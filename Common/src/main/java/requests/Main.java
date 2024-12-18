package requests;

import command_models.Message;
import command_models.MessageFileWrapper;
import commands.CommandSendFile;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Создание объекта MessageFileWrapper
        /*List<Message> l = new ArrayList<Message>();
        //l.add(new Message("subject", "from", "to", "body"));
        //l.add(new Message("subject", "from", "to", "body"));
        RequestGetMessage r = new RequestGetMessage(l);

        String xml = r.serializeToXML();

        System.out.println("Serialized XML:\n" + xml);


        RequestGetMessage de = RequestGetMessage.deserializeFromStr(xml);
        System.out.println("Deserialized: " + de);*/


        List<MessageFileWrapper> files = new ArrayList<>();
        //files.add(new MessageFileWrapper("filename", "from", "to", "body"));

        RequestGetFile rf = new RequestGetFile(files);
        //String xml = rf.serializeToXML();

        //System.out.println("Serialized XML:\n" + xml);
        //files.add(new MessageFileWrapper("filename", "from", "to", "body"));
        //files.add(new MessageFileWrapper("filename", "from", "to", "body"));;

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <RequestGetFile> <RequestType>GET_FILE</RequestType>  </RequestGetFile>";
        RequestGetFile de = RequestGetFile.deserializeFromStr(xml);
        System.out.println(de == null);
        System.out.println("Deserialized: " + de);

    }
}
