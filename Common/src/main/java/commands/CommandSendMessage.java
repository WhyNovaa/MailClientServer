package commands;

import command_models.Message;
import command_models.XMLUtils;
import org.xml.sax.SAXException;
import tools.Separator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CommandSendMessage")
public class CommandSendMessage extends Command {

    @XmlElement(name = "mes")
    private Message mes;

    public CommandSendMessage(Message mes, String jwt_token) {
        super(CommandType.SEND_MESSAGE, jwt_token);
        this.mes = mes;
    }

    public CommandSendMessage() {
        super(CommandType.REGISTER, " ");
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
        return this.getType().toString() + Separator.SEPARATOR + mes.serializeToStr() + Separator.SEPARATOR + getJwtToken();
    }

    public Message getMessage() {
        return mes;
    }

    public String serializeToXML() {
        try {
            return XMLUtils.objectToXML(this);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static CommandSendMessage deserializeFromXML(String xml) {
        try {
            return XMLUtils.xmlToObject(xml, CommandSendMessage.class);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
