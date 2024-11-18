package command_models;

import tools.Separator;

public class Authorization {
    private String login;
    private String password;

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

    public Authorization(String log, String pas){
        this.login = log;
        this.password = pas;
    }

    public String serializeToString(){
        return this.login + Separator.SEPARATOR + this.password;
    }

    public static Authorization deserializeFromString(String str){
        String[] arr = str.split(Separator.SEPARATOR);
        return new Authorization(arr[0], arr[1]);
    }
}
