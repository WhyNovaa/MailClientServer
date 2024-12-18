package requests;

import command_models.XMLUtils;

import javax.xml.bind.annotation.*;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RequestRegistration")
public class RequestRegistration extends Request {

    @XmlElement(name = "Registered")
    private Boolean registered;

    private static final String state = "registered";

    public String getState() {
        return state;
    }

    public RequestRegistration(Boolean flag) {
        super(RequestType.REGISTER);
        this.registered = flag;
    }

    public RequestRegistration(String str) {
        super(RequestType.REGISTER);
        try {
            this.registered = check(str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RequestRegistration() {
        super(RequestType.REGISTER);
    }

    private Boolean check(String str) throws Exception {
        if (str.equals(state)) return true;
        if (str.equals("not " + state)) return false;
        throw new Exception("incorrect state of registration");
    }

    public Boolean isRegistered() {
        return registered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestRegistration req = (RequestRegistration) o;

        return Objects.equals(this.registered, req.registered) && Objects.equals(this.getType(), req.getType());
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

    public static RequestRegistration deserializeFromStr(String xml) {
        try {
            return (RequestRegistration) XMLUtils.xmlToObject(xml, RequestRegistration.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}