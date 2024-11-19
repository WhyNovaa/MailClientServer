package commands;

import command_models.Message;
import tools.Separator;

import java.util.Objects;

public class CommandGetMessage extends Command {
    private Message mes;

    public CommandGetMessage(Message mes) {
        super(CommandType.GET_MESSAGE);
        this.mes = mes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandGetMessage commandGetMessage = (CommandGetMessage) o;

        return Objects.equals(this.mes, commandGetMessage.mes)
                && Objects.equals(this.getType(), commandGetMessage.getType());
    }

    @Override
    public String serializeToStr() {
        return this.getType().toString() + Separator.SEPARATOR + mes.serializeToStr();
    }

    public Message getMessage() {
        return mes;
    }
}
