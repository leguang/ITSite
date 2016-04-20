package cn.itsite.utils;

public class ConstantsUtils {

    /**
     * 常用接口、url
     */
    public static final String DOMAIN_1 = "itsite.cn";
    public static final String DOMAIN_2 = "it-site";
    public static final String SERVER_URL = "http://itsite.cn/json/constants.json";
    public static final String LOGO_URL = "http://7xlrph.com1.z0.glb.clouddn.com/logo.png";
    public static final String SITE_URL = "http://www.itsite.cn/";
    public static final String WEATHER_URL = "http://apicloud.mob.com/v1/weather/query?key=10fe7d0582836&city=";

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    /**
     * 腾讯开发平台应用ID
     */
    public static final String QQ_APP_ID = "1105136720";
    /**
     * 用户信息
     */
    public static final String USERINFO_NICKNAME = "userinfo_nickname";
    public static final String USERINFO_FIGUREURL = "userinfo_figureurl";
    public static final String USERINFO_LOGIN_TYPE = "userinfo_login_type";

    /**
     * 有效时间
     */
    public static final Long VALID_TIME = 1000l;
    /**
     * 登陆类型
     */
    public static final int ERROR_LOGIN_TYPE = -1;
    public static final int MOBILE_LOGIN = 0;
    public static final int QQ_LOGIN = 1;
    public static final int WEIXIN_LOGIN = 2;
    public static final int WEIBO_LOGIN = 3;
    public static final String ISLOGIN = "islogin";

}
