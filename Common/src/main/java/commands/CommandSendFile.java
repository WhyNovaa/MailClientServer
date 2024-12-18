package commands;

import command_models.MessageFileWrapper;
import command_models.XMLUtils;
import org.xml.sax.SAXException;
import tools.Separator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CommandSendFile")
public class CommandSendFile extends Command {

    @XmlElement(name = "file")
    private MessageFileWrapper file;

    public CommandSendFile() {
        super(CommandType.SEND_FILE, " ");
    }

    public CommandSendFile(MessageFileWrapper file, String jwt_token) {
        super(CommandType.SEND_FILE, jwt_token);
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandSendFile commandSendFile = (CommandSendFile) o;

        return Objects.equals(this.file, commandSendFile.file)
                && Objects.equals(this.getType(), commandSendFile.getType());
    }

    @Override
    public String serializeToStr() {
        return this.getType().toString() + Separator.SEPARATOR + file.serializeToStr() + Separator.SEPARATOR + getJwtToken();
    }

    public MessageFileWrapper getFile() {
        return file;
    }

    public void setFile(MessageFileWrapper file) {
        this.file = file;
    }

    public String serializeToXML() {
        try {
            return XMLUtils.objectToXML(this);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static CommandSendFile deserializeFromXML(String xml) {
        try {
            return XMLUtils.xmlToObject(xml, CommandSendFile.class);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
