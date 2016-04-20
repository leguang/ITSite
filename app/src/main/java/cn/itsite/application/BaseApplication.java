package cn.itsite.application;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;
import com.tencent.tauth.Tencent;

import cn.itsite.bean.CategoriesData;
import cn.itsite.bean.LoginInfoData;
import cn.itsite.bean.UserInfoData;
import cn.itsite.utils.ConstantsUtils;


public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";
    public static CategoriesData categoriesData;
    public static UserInfoData userInfo;
    public static LoginInfoData loginInfo;
    public static final Bus bus = new Bus();
    private static BaseApplication application;
    private static Context context;
    public static Boolean islogin;
    public static Tencent mTencent;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        mTencent = Tencent.createInstance(ConstantsUtils.QQ_APP_ID, this.getApplicationContext());
        context = getApplicationContext();
        initUserConfig();
    }

    private void initUserConfig() {
        userInfo = UserInfoData.getUserInfoInstance();
        loginInfo = LoginInfoData.getLoginInfoInstance();
    }

    public static synchronized BaseApplication getBaseApplication() {
        return application;
    }

    public static Context getContext() {
        return context;
    }

}
