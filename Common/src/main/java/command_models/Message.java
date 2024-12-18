package command_models;

import org.xml.sax.SAXException;
import tools.Separator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Message")
public class Message {
    @XmlElement(name = "from")
    private String from;

    @XmlElement(name = "to")
    private String to;

    @XmlElement(name = "subject")
    private String subject;

    @XmlElement(name = "body")
    private String body;

    // Пустой конструктор для JAXB
    public Message() {}

    public Message(String subject, String from, String to, String body) {
        this.subject = subject;
        this.from = from;
        this.to = to;
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message mes = (Message) o;

        return this.subject.equals(mes.subject) && this.from.equals(mes.from)
                && this.to.equals(mes.to) && this.body.equals(mes.body);
    }

    public String serializeToStr(){
        return this.subject + Separator.SEPARATOR + this.from
                + Separator.SEPARATOR + this.to + Separator.SEPARATOR + this.body;
    }

    public static Message deserializeFromStr(String str) {
        String[] arr = str.split(Separator.SEPARATOR);
        return new Message(arr[0], arr[1], arr[2], arr[3]);
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
    public static Message deserializeFromXML(String xml) {
        try {
            return XMLUtils.xmlToObject(xml, Message.class);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
