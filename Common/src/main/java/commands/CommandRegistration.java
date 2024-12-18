package commands;

import command_models.Registration;
import command_models.XMLUtils;
import org.xml.sax.SAXException;
import tools.Separator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CommandRegistration")
public class CommandRegistration extends Command {
    @XmlElement(name = "reg")
    private Registration reg;

    public CommandRegistration() {
        super(CommandType.REGISTER, " ");
    }

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
        return getType().toString() + Separator.SEPARATOR + reg.serializeToStr() + Separator.SEPARATOR + getJwtToken();
    }

    public Registration getRegistration() {
        return reg;
    }

    public void setRegistration(Registration reg) {
        this.reg = reg;
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
    public static CommandRegistration deserializeFromXML(String xml) {
        try {
            return XMLUtils.xmlToObject(xml, CommandRegistration.class);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
