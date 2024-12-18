package commands;

import command_models.XMLUtils;
import org.xml.sax.SAXException;
import tools.Separator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CommandGetMessage")
public class CommandGetMessage extends Command {

    public CommandGetMessage() {
        super(CommandType.GET_MESSAGE, " ");
    }

    public CommandGetMessage(String jwt_token) {
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

    // Метод для сериализации в XML
    public String serializeToXML() {
        try {
            return XMLUtils.objectToXML(this);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для десериализации из XML
    public static CommandGetMessage deserializeFromXML(String xml) {
        try {
            return XMLUtils.xmlToObject(xml, CommandGetMessage.class);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
