package commands;

import command_models.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Command")
@XmlSeeAlso({CommandAuthorization.class, CommandRegistration.class, CommandSendMessage.class, CommandSendFile.class, CommandGetMessage.class})
public abstract class Command {
    @XmlElement(name = "CommandType")
    private CommandType type;

    @XmlElement(name = "JwtToken")
    private String jwt_token;

    // Конструкторы, геттеры и сеттеры

    public Command(CommandType type, String jwt_token) {
        this.type = type;
        this.jwt_token = jwt_token;
    }
    public Command(){}

    public CommandType getType() {
        return type;
    }

    public String getJwtToken() {
        return jwt_token;
    }

    public abstract boolean equals(Object o);

    public abstract String serializeToStr();

    public String serializeToXML() {
        try {
            return XMLUtils.objectToXML(this);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Command deserializeFromStr(String str) {
        try {
            return XMLUtils.xmlToObject(str, Command.class);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }


}

