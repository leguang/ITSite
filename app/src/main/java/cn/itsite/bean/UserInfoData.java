package cn.itsite.bean;

public class UserInfoData {

    private static UserInfoData userInfoData = null;
    public String nickname;
    public String figureurUrl;


    private UserInfoData() {
    }

    public static UserInfoData getUserInfoInstance() {
        if (userInfoData == null) {
            synchronized (UserInfoData.class) {
                if (userInfoData == null) {
                    userInfoData = new UserInfoData();
                }
            }
        }
        return userInfoData;
    }

}
