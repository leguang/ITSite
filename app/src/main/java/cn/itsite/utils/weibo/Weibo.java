package cn.itsite.utils.weibo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import cn.itsite.application.BaseApplication;
import cn.itsite.bean.ErrorInfo;
import cn.itsite.utils.ConstantsUtils;
import cn.itsite.utils.SpUtils;

public class Weibo {

    public Context mContext;

    /**
     * 新浪App信息，Appkey在ConstantsUtils中
     */
    public AuthInfo mAuthInfo;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    public Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    public SsoHandler mSsoHandler;
    /**
     * 用户信息接口
     */
    public UsersAPI mUsersAPI;

    public Weibo(Context mContext, AuthInfo mAuthInfo) {
        this.mContext = mContext;
        this.mAuthInfo = mAuthInfo;
        this.mSsoHandler = new SsoHandler((Activity) mContext, mAuthInfo);
    }


    public void authorize() {
        mSsoHandler.authorize(new AuthListener());
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link # onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(mContext, mAccessToken);
                //获取用户信息
                mUsersAPI = new UsersAPI(mContext, ConstantsUtils.APP_KEY, mAccessToken);
                long uid = Long.parseLong(mAccessToken.getUid());
                mUsersAPI.show(uid, userInfolistener);

            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = null;
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(mContext,
                    "取消", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(mContext,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener userInfolistener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {

                    String figureurl = user.profile_image_url;
                    BaseApplication.userInfo.figureurUrl = figureurl;
                    SpUtils.setString(mContext, ConstantsUtils.USERINFO_FIGUREURL, figureurl);

                    String nickname = user.screen_name;
                    BaseApplication.userInfo.nickname = nickname;
                    SpUtils.setString(mContext, ConstantsUtils.USERINFO_NICKNAME, nickname);

                    SpUtils.setBoolean(mContext, ConstantsUtils.ISLOGIN, true);
                    SpUtils.setInt(mContext, ConstantsUtils.USERINFO_LOGIN_TYPE, ConstantsUtils.WEIBO_LOGIN);

                    BaseApplication.bus.post(BaseApplication.userInfo);
                    ((Activity) mContext).finish();

                } else {
                    Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(mContext, info.toString(), Toast.LENGTH_LONG).show();
        }
    };


}
