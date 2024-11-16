package test;

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
        String[] args = str.split(":");
        String type = args[0];
        Command command = null;
        switch (type) {
            case "AUTHORIZATION":
                command = new CommandAuthorization(new Authorization(args[1], args[2]));
            case "REGISTRATION":
                command = null;

                default: return null;
        }
    }
}

