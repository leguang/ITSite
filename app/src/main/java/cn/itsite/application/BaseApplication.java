package cn.itsite.application;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.squareup.otto.Bus;

import cn.itsite.bean.CategoriesData;
import cn.itsite.bean.UserConfig;
import cn.itsite.utils.ConstantsUtils;
import cn.itsite.utils.SpUtils;


public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";
    public static CategoriesData categoriesData;
    private static boolean sIsAtLeastGB;
    public static UserConfig userConfig;
    public static final Bus bus = new Bus();
    private static BaseApplication application;
    private static Context context;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sIsAtLeastGB = true;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = getApplicationContext();
        initUserConfig();
    }

    private void initUserConfig() {
        userConfig = UserConfig.getUserConfigInstance();
        userConfig.nickname = SpUtils.getString(this, ConstantsUtils.NICKNAME, null);
        userConfig.figureurUrl = SpUtils.getString(this, ConstantsUtils.FIGUREURL, null);
    }

    public static synchronized BaseApplication getBaseApplication() {
        return application;
    }

    public static Context getContext() {
        return context;
    }

}
