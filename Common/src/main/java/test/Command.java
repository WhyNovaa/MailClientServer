package test;

public abstract class Command {
    private CommandType type;

    public Command(CommandType type, String body) {
        this.type = type;
    }

    public CommandType getType() {
        return type;
    }

    public abstract String serializeToStr();
    public abstract Command deserializeFromStr(String str);
}

