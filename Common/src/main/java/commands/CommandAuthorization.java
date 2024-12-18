package commands;

import command_models.Authorization;
import command_models.XMLUtils;
import org.xml.sax.SAXException;
import tools.Separator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CommandAuthorization")
public class CommandAuthorization extends Command {
    @XmlElement(name = "auth")
    private Authorization auth;

    public CommandAuthorization(Authorization auth) {
        super(CommandType.LOGIN, " ");
        this.auth = auth;
    }

    public CommandAuthorization() {
        super(CommandType.LOGIN, " ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandAuthorization commandAuthorization = (CommandAuthorization) o;

        return Objects.equals(this.auth, commandAuthorization.auth)
                && Objects.equals(this.getType(), commandAuthorization.getType());
    }

    @Override
    public String serializeToStr() {
        return getType().toString() + Separator.SEPARATOR + auth.serializeToStr() + Separator.SEPARATOR + getJwtToken();
    }

    public Authorization getAuthorization() {
        return auth;
    }

    public void setAuthorization(Authorization auth) {
        this.auth = auth;
    }

    public String serializeToXML() {
        try {
            return XMLUtils.objectToXML(this);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static CommandAuthorization deserializeFromXML(String xml) {
        try {
            return XMLUtils.xmlToObject(xml, CommandAuthorization.class);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
