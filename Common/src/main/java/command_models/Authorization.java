package command_models;

import commands.CommandAuthorization;
import tools.Separator;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Authorization auth = (Authorization) o;

        return this.login.equals(auth.login) && this.password.equals(auth.password);
    }

    public String serializeToStr(){
        return this.login + Separator.SEPARATOR + this.password;
    }

    public static Authorization deserializeFromStr(String str){
        String[] arr = str.split(Separator.SEPARATOR);
        return new Authorization(arr[0], arr[1]);
    }
}
