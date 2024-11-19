package command_models;

import tools.Separator;

public class Registration {
    private String login;
    private String password;

    public Registration(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String serializeToString(){
        return this.login + Separator.SEPARATOR + this.password;
    }
}
