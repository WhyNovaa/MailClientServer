package commands;

import command_models.*;
import tools.Separator;

public abstract class Command {
    private CommandType type;
    private String jwt_token;

    public Command(CommandType type, String jwt_token) {
        this.type = type;
        this.jwt_token = jwt_token;
    }

    public CommandType getType() {
        return type;
    }
    public String getJwtToken() {
        return jwt_token;
    }

    public abstract boolean equals(Object o);

    public abstract String serializeToStr();

    public static Command deserializeFromStr(String str) {
        String[] args = str.split(Separator.SEPARATOR);
        String type = args[0];
        Command command = switch (type) {
            case "LOGIN" -> new CommandAuthorization(new Authorization(args[1], args[2]));
            case "REGISTER" -> new CommandRegistration(new Registration(args[1], args[2]));
            case "SEND_MESSAGE" -> new CommandSendMessage(new Message(args[1], args[2], args[3], args[4]), args[5]);
            case "SEND_FILE" -> new CommandSendFile(new MessageFileWrapper(args[1], args[2], args[3], args[4]), args[5]);
            case "GET_MESSAGE" -> new CommandGetMessage(args[1]);
            default -> null;
        };

        return command;
    }
}

