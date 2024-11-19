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


    public abstract boolean equals(Object o);

    public abstract String serializeToStr();

    public static Command deserializeFromStr(String str) {
        String[] args = str.split(Separator.SEPARATOR);
        String type = args[0];
        Command command = switch (type) {
            case "LOGIN" -> new CommandAuthorization(new Authorization(args[1], args[2]));
            case "REGISTER" -> new CommandRegistration(new Registration(args[1], args[2]));
            case "SEND_MESSAGE" -> new CommandSendMessage(new Message(args[1], args[2], args[3], args[4]));
            case "GET_MESSAGE" -> new CommandGetMessage(new Message(args[1], args[2], args[3], args[4]));
            default -> null;
        };

        return command;
    }
}

