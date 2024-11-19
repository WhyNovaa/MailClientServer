package commands;

import command_models.Message;

public class CommandGetMessage extends Command {
    private Message mes;

    public CommandGetMessage(Message mes) {
        super(CommandType.GET_MESSAGE);
        this.mes = mes;
    }

    @Override
    public String serializeToStr() {
        return ""; // TODO
    }

    public Message getMessage() {
        return mes;
    }
}
