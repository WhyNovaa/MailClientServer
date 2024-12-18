package requests;

import command_models.XMLUtils;

import javax.xml.bind.annotation.*;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RequestAuthorization")
public class RequestAuthorization extends Request {

    private static final String state = "authorized";

    @XmlElement(name = "Authorized")
    private Boolean authorized;

    @XmlElement(name = "JwtToken")
    private String jwt_token;

    public String getState() {
        return state;
    }

    public RequestAuthorization(Boolean flag, String jwt) {
        super(RequestType.LOGIN);
        this.authorized = flag;
        this.jwt_token = jwt;
    }

    public RequestAuthorization(String authorization, String jwt) {
        super(RequestType.LOGIN);
        this.jwt_token = jwt;
        try {
            this.authorized = check(authorization);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RequestAuthorization() {
        super(RequestType.LOGIN);
    }

    private Boolean check(String str) throws Exception {
        if (str.equals(state)) return true;
        if (str.equals("not " + state)) return false;
        throw new Exception("incorrect state of authorization");
    }

    public Boolean isAuthorized() {
        return authorized;
    }

    public String getJwt_token() {
        return jwt_token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestAuthorization req = (RequestAuthorization) o;

        return Objects.equals(this.jwt_token, req.jwt_token) && Objects.equals(this.authorized, req.authorized) && Objects.equals(this.getType(), req.getType());
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

    public static RequestAuthorization deserializeFromStr(String xml) {
        try {
            return (RequestAuthorization) XMLUtils.xmlToObject(xml, RequestAuthorization.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}