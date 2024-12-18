package requests;

import command_models.XMLUtils;

import javax.xml.bind.annotation.*;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RequestSendMessage")
public class RequestSendMessage extends Request {

    @XmlElement(name = "IsSent")
    private Boolean is_sent;

    private static final String state = "sent";

    public String getState() {
        return state;
    }

    public RequestSendMessage(Boolean is_sent) {
        super(RequestType.SEND_MESSAGE);
        this.is_sent = is_sent;
    }

    public RequestSendMessage(String str) {
        super(RequestType.SEND_MESSAGE);
        try {
            this.is_sent = check(str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RequestSendMessage() {
        super(RequestType.SEND_MESSAGE);
    }

    private Boolean check(String str) throws Exception {
        if (str.equals(state)) return true;
        if (str.equals("not " + state)) return false;
        throw new Exception("incorrect state of sending");
    }

    public Boolean isSent() {
        return is_sent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestSendMessage req = (RequestSendMessage) o;

        return Objects.equals(this.is_sent, req.is_sent) && Objects.equals(this.getType(), req.getType());
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

    public static RequestSendMessage deserializeFromStr(String xml) {
        try {
            return (RequestSendMessage) XMLUtils.xmlToObject(xml, RequestSendMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
