package commands;

import command_models.Message;

public class CommandSendMessage extends Command {
    private Message mes;

    public CommandSendMessage(Message mes) {
        super(CommandType.SEND_MESSAGE);
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
