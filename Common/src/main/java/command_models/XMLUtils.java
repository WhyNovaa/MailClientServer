package command_models;

import commands.Command;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;

public class XMLUtils {
    static String schemaFilePath = "C:/Projects/Java/mail server/MailClientServer/path/to/your/schema.xsd";

    public static String objectToXML(Object object) throws JAXBException {
        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(object, sw);
        return sw.toString();
    }

    public static <T> T xmlToObject(String xml, Class<T> clazz) throws JAXBException, SAXException, SAXException {
        StringReader sr = new StringReader(xml);
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // Указываем схему для валидации
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new File("C:\\Projects\\Java\\mail server\\MailClientServer\\Common\\src\\main\\java\\schemas\\schema.xsd"));
        unmarshaller.setSchema(schema);

        return clazz.cast(unmarshaller.unmarshal(sr));
    }
}
