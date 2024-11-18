package commands;

import command_models.*;
import tools.Separator;

public abstract class Command {
    private CommandType type;

    public Command(CommandType type) {
        this.type = type;
    }

    public CommandType getType() {
        return type;
    }

    public abstract String serializeToStr();

    public static Command deserializeFromStr(String str) {
        String[] args = str.split(Separator.SEPARATOR);
        String type = args[0];
        Command command = null;
        switch (type) {
            case "AUTHORIZATION":
                command = new CommandAuthorization(new Authorization(args[1], args[2]));
            case "REGISTRATION":
                command = null; // TODO
            case "SEND_MESSAGE":
                command = null; // TODO
            case "GET_MESSAGE":
                command = null; // TODO
            default:
                command = null;
        }
        return command;
    }
}

