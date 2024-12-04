package commands;

import command_models.FileWrapper;
import command_models.Message;
import tools.Separator;

import java.util.Objects;

public class CommandSendFile extends Command {

    private FileWrapper file;

    public CommandSendFile(FileWrapper file, String jwt_token) {
        super(CommandType.SEND_FILE, jwt_token);
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        commands.CommandSendFile commandSendMessage = (commands.CommandSendFile) o;

        return Objects.equals(this.file, commandSendMessage.file)
                && Objects.equals(this.getType(), commandSendMessage.getType());
    }

    @Override
    public String serializeToStr() {
        return this.getType().toString() + Separator.SEPARATOR + file.serializeToStr() + Separator.SEPARATOR + getJwtToken();
    }

    public FileWrapper getFile() {
        return file;
    }
}