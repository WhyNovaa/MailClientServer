package commands;

public class CommandGetMessage extends Command {
    public CommandGetMessage(){
        super(CommandType.GET_MESSAGE);
    }

    @Override
    public boolean equals(Object o) {
        return true;
    }

    @Override
    public String serializeToStr() {
        return this.getType().toString();
    }
}
