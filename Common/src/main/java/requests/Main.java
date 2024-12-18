package requests;

import command_models.MessageFileWrapper;
import org.xml.sax.SAXException;
import requests.RequestGetFile;
import command_models.XMLUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<MessageFileWrapper> files = new ArrayList<>();
        RequestGetFile rf = new RequestGetFile(files);

        try {
            // Сериализация в XML
            JAXBContext context = JAXBContext.newInstance(RequestGetFile.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            // Сериализация в строку
            java.io.StringWriter sw = new java.io.StringWriter();
            sw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
            marshaller.marshal(rf, sw);
            String xmlString = sw.toString();

            System.out.println("Serialized XML:\n" + xmlString);

            // Десериализация
            RequestGetFile deserializedRf = (RequestGetFile) XMLUtils.xmlToObject(xmlString, RequestGetFile.class);
            List<MessageFileWrapper> deserializedFiles = deserializedRf.getFiles();
            System.out.println("Deserialized object files: " + deserializedFiles.size()); // Ожидается вывод: 0
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
        }
    }
}
