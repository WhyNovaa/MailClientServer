package commands;

import command_models.Message;
import tools.Separator;

import java.util.Objects;

public class CommandSendMessage extends Command {
    private Message mes;

    public CommandSendMessage(Message mes, String jwt_token) {
        super(CommandType.SEND_MESSAGE, jwt_token);
        this.mes = mes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandSendMessage commandSendMessage = (CommandSendMessage) o;

        return Objects.equals(this.mes, commandSendMessage.mes)
                && Objects.equals(this.getType(), commandSendMessage.getType());
    }

    @Override
    public String serializeToStr() {
        return this.getType().toString() + Separator.SEPARATOR + mes.serializeToStr();
    }

    public Message getMessage() {
        return mes;
    }
}
