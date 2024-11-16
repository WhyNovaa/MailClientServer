package test;

public class CommandAuthorization extends Command{
    private Authorization auth;

    public static final String SEPARATOR = ":";
    public CommandAuthorization(Authorization auth) {
        super(CommandType.LOGIN);
        this.auth = auth;
    }

    @Override
    public String serializeToStr() {
        return getType().toString() + SEPARATOR + auth.serializeToString();
    }
}