package command_models;

import org.xml.sax.SAXException;
import tools.Separator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MessageFileWrapper")
public class MessageFileWrapper {
    @XmlElement(name = "from")
    private String from;

    @XmlElement(name = "to")
    private String to;

    @XmlElement(name = "fileName")
    private String fileName;

    @XmlElement(name = "fileContent")
    private String fileContent;

    // Пустой конструктор для JAXB
    public MessageFileWrapper() {}

    public MessageFileWrapper(String fileName, String from, String to, String fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.to = to;
        this.from = from;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileContent() {
        return fileContent;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageFileWrapper file = (MessageFileWrapper) o;

        return this.fileName.equals(file.fileName) && this.fileContent.equals(file.fileContent)
                && this.from.equals(file.from)
                && this.to.equals(file.to);
    }

    public static MessageFileWrapper deserializeFromStr(String str){
        String[] arr = str.split(Separator.SEPARATOR);
        return new MessageFileWrapper(arr[0],arr[1],arr[2],arr[3]);
    }

    public String serializeToStr() {
        return this.fileName + Separator.SEPARATOR + this.from
                + Separator.SEPARATOR + this.to + Separator.SEPARATOR + this.fileContent;
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
    public static MessageFileWrapper deserializeFromXML(String xml) {
        try {
            return XMLUtils.xmlToObject(xml, MessageFileWrapper.class);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
