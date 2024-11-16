package test;

public class Authorization {
    private static String password;
    private static String login;

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        Authorization.login = login;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Authorization.password = password;
    }

    Authorization(String log, String pas){
        this.login = log;
        this.password = pas;
    }
}
