package cn.itsite.bean;

public class UserConfig {

    private static UserConfig userConfig = null;
    public String nickname;
    public String figureurUrl;

    private UserConfig(){}

    public static UserConfig getUserConfigInstance() {
        if (userConfig == null) {
            synchronized (UserConfig.class) {
                if (userConfig == null) {
                    userConfig = new UserConfig();
                }
            }
        }
        return userConfig;
    }

    @Override
    public String toString() {
        return "UserConfig{" +
                "nickname='" + nickname + '\'' +
                ", figureurUrl='" + figureurUrl + '\'' +
                '}';
    }
}
