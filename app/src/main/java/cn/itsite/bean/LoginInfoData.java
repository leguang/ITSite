package cn.itsite.bean;

public class LoginInfoData {

    private static LoginInfoData loginInfo = null;
    public String token;
    public String expires;
    public String openId;
    public int loginType;

    private LoginInfoData() {
    }

    public static LoginInfoData getLoginInfoInstance() {
        if (loginInfo == null) {
            synchronized (LoginInfoData.class) {
                if (loginInfo == null) {
                    loginInfo = new LoginInfoData();
                }
            }
        }
        return loginInfo;
    }

}
