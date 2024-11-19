package commands;

import command_models.Authorization;
import tools.Separator;

import java.util.Objects;

public class CommandAuthorization extends Command {
    private Authorization auth;

    public CommandAuthorization(Authorization auth) {
        super(CommandType.LOGIN);
        this.auth = auth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandAuthorization commandAuthorization = (CommandAuthorization) o;

        return Objects.equals(this.auth, commandAuthorization.auth)
                && Objects.equals(this.getType(), commandAuthorization.getType());
    }

    @Override
    public String serializeToStr() {
        return getType().toString() + Separator.SEPARATOR + auth.serializeToStr();
    }

    public Authorization getAuthorization() {
        return auth;
    }
}