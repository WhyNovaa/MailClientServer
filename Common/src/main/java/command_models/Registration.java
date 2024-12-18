package command_models;

import org.xml.sax.SAXException;
import tools.Separator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Registration")
public class Registration {
    @XmlElement(name = "login")
    private String login;

    @XmlElement(name = "password")
    private String password;

    // Пустой конструктор для JAXB
    public Registration() {}

    public Registration(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Registration reg = (Registration) o;

        return this.login.equals(reg.login) && this.password.equals(reg.password);
    }

    public String serializeToStr(){
        return this.login + Separator.SEPARATOR + this.password;
    }

    public static Registration deserializeFromStr(String str) {
        String[] arr = str.split(Separator.SEPARATOR);
        return new Registration(arr[0], arr[1]);
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
    public static Registration deserializeFromXML(String xml) {
        try {
            return XMLUtils.xmlToObject(xml, Registration.class);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
