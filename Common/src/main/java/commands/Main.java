package commands;

import command_models.MessageFileWrapper;
import commands.CommandSendFile;

import javax.xml.bind.JAXBException;

public class Main {
    public static void main(String[] args) {
        // Создание объекта MessageFileWrapper
        MessageFileWrapper fileWrapper = new MessageFileWrapper("file.txt", "from@example.com", "to@example.com", "File content");
        CommandSendFile command = new CommandSendFile(fileWrapper, "sample_jwt_token");

        // Сериализация объекта CommandSendFile в XML
        String xml = command.serializeToXML();
        System.out.println("Serialized XML:\n" + xml);

        // Десериализация объекта CommandSendFile из XML
        CommandSendFile deserializedCommand = CommandSendFile.deserializeFromXML(xml);
        System.out.println("Deserialized CommandSendFile: " + deserializedCommand.serializeToStr());

    }
}
