package requests;

import command_models.MessageFileWrapper;
import command_models.Message;
import command_models.XMLUtils;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Request")
@XmlSeeAlso({RequestAuthorization.class, RequestRegistration.class, RequestGetMessage.class, RequestGetFile.class, RequestSendMessage.class})
public abstract class Request {

    @XmlElement(name = "RequestType")
    private RequestType type;

    // Constructors, getters, and setters

    public Request(RequestType type) {
        this.type = type;
    }

    public Request() {
    }

    public RequestType getType() {
        return type;
    }

    public abstract boolean equals(Object o);

    public String serializeToXML() {
        try {
            return XMLUtils.objectToXML(this);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}