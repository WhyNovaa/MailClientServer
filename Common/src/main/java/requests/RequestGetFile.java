package requests;

import command_models.MessageFileWrapper;
import command_models.XMLUtils;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RequestGetFile")
public class RequestGetFile extends Request {

    @XmlElement(name = "Files")
    private List<MessageFileWrapper> files;

    public RequestGetFile(List<MessageFileWrapper> files) {
        super(RequestType.GET_FILE);
        this.files = files;
    }

    public RequestGetFile() {
        super(RequestType.GET_FILE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestGetFile req = (RequestGetFile) o;

        return Objects.equals(this.files, req.files)
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

    public static RequestGetFile deserializeFromStr(String xml) {
        try {
            return (RequestGetFile) XMLUtils.xmlToObject(xml, RequestGetFile.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MessageFileWrapper> getFiles() {
        return files;
    }
}
