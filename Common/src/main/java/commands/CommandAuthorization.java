package commands;

import command_models.Authorization;
import tools.Separator;

public class CommandAuthorization extends Command {
    private Authorization auth;

    public CommandAuthorization(Authorization auth) {
        super(CommandType.LOGIN);
        this.auth = auth;
    }

    @Override
    public String serializeToStr() {
        return getType().toString() + Separator.SEPARATOR + auth.serializeToString();
    }
}