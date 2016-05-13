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
     * 腾讯开发平台应用ID
     */
    public static final String WEIXIN_APP_ID = "wx013cfac3ddb4bc4c";
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
    public static final int WEIXIN_LOGIN = 3;
    public static final int WEIBO_LOGIN = 2;
    public static final String ISLOGIN = "islogin";


    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    public static final String APP_KEY = "73956445";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "http://itsite.cn";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

}
