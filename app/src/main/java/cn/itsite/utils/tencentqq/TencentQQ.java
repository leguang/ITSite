package cn.itsite.utils.tencentqq;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import cn.itsite.application.BaseApplication;
import cn.itsite.utils.ConstantsUtils;
import cn.itsite.utils.SpUtils;
import cn.itsite.utils.ToastUtils;

/**
 * Created by Administrator on 2016/5/2 0002.
 */
public class TencentQQ {
    public Context mContext;
    public Tencent mTencent;
    private UserInfo mInfo;


    public TencentQQ(Context mContext, Tencent mTencent) {
        this.mContext = mContext;
        this.mTencent = Tencent.createInstance(ConstantsUtils.QQ_APP_ID, mContext.getApplicationContext());
    }

    public void login() {
        mTencent.login((Activity) mContext, "all", loginListener);
    }

    public IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
            updateUserInfo();
        }
    };

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                return;
            }
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onCancel() {

        }
    }

    private void initOpenidAndToken(JSONObject jsonObject) {

        String token = jsonObject.optString(Constants.PARAM_ACCESS_TOKEN);
        String expires = jsonObject.optString(Constants.PARAM_EXPIRES_IN);
        String openId = jsonObject.optString(Constants.PARAM_OPEN_ID);

        BaseApplication.loginInfo.token = token;
        BaseApplication.loginInfo.expires = expires;
        BaseApplication.loginInfo.openId = openId;

        SpUtils.setString(mContext, Constants.PARAM_ACCESS_TOKEN, token);
        SpUtils.setString(mContext, Constants.PARAM_EXPIRES_IN, expires);
        SpUtils.setString(mContext, Constants.PARAM_OPEN_ID, openId);

        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openId);
        }
    }


    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            mInfo = new UserInfo(mContext, mTencent.getQQToken());
            mInfo.getUserInfo(userInfolistener);

        } else {
        }
    }


    IUiListener userInfolistener = new IUiListener() {

        @Override
        public void onError(UiError e) {
            ToastUtils.showToast(mContext, "错误：" + e.errorMessage);
        }

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                return;
            }
            JSONObject json = (JSONObject) response;

            if (json.has("figureurl")) {
                String figureurl = json.optString("figureurl_qq_1");
                BaseApplication.userInfo.figureurUrl = figureurl;
                SpUtils.setString(mContext, ConstantsUtils.USERINFO_FIGUREURL, figureurl);
            }

            if (json.has("nickname")) {
                String nickname = json.optString("nickname");
                BaseApplication.userInfo.nickname = nickname;
                SpUtils.setString(mContext, ConstantsUtils.USERINFO_NICKNAME, nickname);
            }

            SpUtils.setBoolean(mContext, ConstantsUtils.ISLOGIN, true);
            SpUtils.setInt(mContext, ConstantsUtils.USERINFO_LOGIN_TYPE, ConstantsUtils.QQ_LOGIN);

            BaseApplication.bus.post(BaseApplication.userInfo);
            ((Activity) mContext).finish();
        }

        @Override
        public void onCancel() {
            ToastUtils.showToast(mContext, "取消");
        }
    };

}
