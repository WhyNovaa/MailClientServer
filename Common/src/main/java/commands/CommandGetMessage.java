package commands;

import command_models.Message;
import tools.JWT_TOKEN;
import tools.Separator;

import java.util.Objects;

public class CommandGetMessage extends Command {
    public CommandGetMessage(String jwt_token){
        super(CommandType.GET_MESSAGE, jwt_token);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandGetMessage commandGetMessage = (CommandGetMessage) o;

        return Objects.equals(this.getType(), commandGetMessage.getType());
    }

    @Override
    public String serializeToStr() {
        return getType().toString() + Separator.SEPARATOR + getJwtToken();
    }
}
