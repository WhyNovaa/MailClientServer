package commands;

import command_models.Registration;
import tools.Separator;

import java.util.Objects;

public class CommandRegistration extends Command {
    private Registration reg;

    public CommandRegistration(Registration reg) {
        super(CommandType.REGISTER, " ");
        this.reg = reg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandRegistration commandRegistration = (CommandRegistration) o;

        return Objects.equals(this.reg, commandRegistration.reg)
                && Objects.equals(this.getType(), commandRegistration.getType());
    }

    @Override
    public String serializeToStr() {
        return getType().toString() + Separator.SEPARATOR + reg.serializeToStr();
    }

    public Registration getRegistration() {
        return reg;
    }
}