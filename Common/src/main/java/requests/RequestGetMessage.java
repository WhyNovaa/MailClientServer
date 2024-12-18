package requests;

import command_models.Message;
import command_models.XMLUtils;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RequestGetMessage")
public class RequestGetMessage extends Request {

    @XmlElementWrapper(name = "Messages")
    @XmlElement(name = "Message")
    private List<Message> messages;

    public RequestGetMessage(List<Message> messages) {
        super(RequestType.GET_MESSAGE);
        this.messages = messages;
    }

    public RequestGetMessage() {
        super(RequestType.GET_MESSAGE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestGetMessage req = (RequestGetMessage) o;

        return Objects.equals(this.messages, req.messages)
                && Objects.equals(this.getType(), req.getType());
    }

    @Override
    public String serializeToXML() {
        try {
            return XMLUtils.objectToXML(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RequestGetMessage deserializeFromStr(String xml) {
        try {
            return (RequestGetMessage) XMLUtils.xmlToObject(xml, RequestGetMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Message> getMessages() {
        return messages;
    }
}