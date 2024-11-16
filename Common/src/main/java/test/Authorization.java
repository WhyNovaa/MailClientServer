package test;

public class Authorization {
    public static final String SEPARATOR = "\n";
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

    public String serializeToString(){
        return this.login + SEPARATOR + this.password;
    }

    public static Authorization deserializeFromString(String str){
        String[] arr = str.split(SEPARATOR);
        return new Authorization(arr[0], arr[1]);
    }
}
