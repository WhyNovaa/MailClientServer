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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Registration reg = (Registration) o;

        return this.login.equals(reg.login) && this.password.equals(reg.password);
    }

    public String serializeToStr(){
        return this.login + Separator.SEPARATOR + this.password;
    }

    public static Registration deserializeFromStr(String str) {
        String[] arr = str.split(Separator.SEPARATOR);
        return new Registration(arr[0], arr[1]);
    }
}
