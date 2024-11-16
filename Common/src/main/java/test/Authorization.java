package test;

public class Authorization {
    public static final String SEPARATOR = ":";
    private String password;
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
