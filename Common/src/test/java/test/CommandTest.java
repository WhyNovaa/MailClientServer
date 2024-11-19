package test;

import command_models.Authorization;
import command_models.Message;
import command_models.Registration;
import commands.*;
import org.junit.jupiter.api.Test;
import tools.Separator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandTest {
    @Test
    public void isValidCommandAuthorizationSerialization() {
        Authorization auth = new Authorization("admin", "admin");
        CommandAuthorization commandAuthorization = new CommandAuthorization(auth);
        String expected = "LOGIN" + Separator.SEPARATOR + "admin" + Separator.SEPARATOR + "admin";
        assertEquals(expected, commandAuthorization.serializeToStr());
    }

    @Test
    public void isValidCommandRegistrationSerialization() {
        Registration reg = new Registration("admin", "admin");
        CommandRegistration commandRegistration = new CommandRegistration(reg);
        String expected = "REGISTER" + Separator.SEPARATOR + "admin" + Separator.SEPARATOR + "admin";
        assertEquals(expected, commandRegistration.serializeToStr());
    }

    @Test
    public void isValidCommandGetMessageSerialization() {
        Message mes = new Message("subject", "from", "to", "body");
        CommandGetMessage commandGetMessage = new CommandGetMessage(mes);
        String expected = "GET_MESSAGE" + Separator.SEPARATOR + "subject" + Separator.SEPARATOR + "from" + Separator.SEPARATOR + "to" + Separator.SEPARATOR + "body";
        assertEquals(expected, commandGetMessage.serializeToStr());
    }

    @Test
    public void isValidCommandSendMessageSerialization() {
        Message mes = new Message("subject", "from", "to", "body");
        CommandSendMessage commandSendMessage = new CommandSendMessage(mes);
        String expected = "SEND_MESSAGE" + Separator.SEPARATOR + "subject" + Separator.SEPARATOR + "from" + Separator.SEPARATOR + "to" + Separator.SEPARATOR + "body";
        assertEquals(expected, commandSendMessage.serializeToStr());
    }

    @Test
    public void isValidCommandDeserializationToCommandAuthorization() {
        //CommandAuthorization
        Authorization auth = new Authorization("admin", "admin");
        CommandAuthorization expected = new CommandAuthorization(auth);
        String serializedCommand = expected.serializeToStr();

        CommandAuthorization com = (CommandAuthorization) Command.deserializeFromStr(serializedCommand);
        assertEquals(expected, com);
    }

    @Test
    public void isValidCommandDeserializationToCommandRegistration() {
        //CommandRegistration
        Registration reg = new Registration("admin", "admin");
        CommandRegistration expected = new CommandRegistration(reg);
        String serializedCommand = expected.serializeToStr();

        CommandRegistration com = (CommandRegistration) Command.deserializeFromStr(serializedCommand);
        assertEquals(expected, com);
    }

    @Test
    public void isValidCommandDeserializationToCommandGetMessage() {
        //CommandGetMessage
        Message mes = new Message("subject", "from", "to", "body");
        CommandGetMessage expected = new CommandGetMessage(mes);

        String serializedCommand = expected.serializeToStr();

        CommandGetMessage com = (CommandGetMessage) Command.deserializeFromStr(serializedCommand);
        assertEquals(expected, com);
    }

    @Test
    public void isValidCommandDeserializationToCommandSendMessage() {
        //CommandSendMessage
        Message mes = new Message("subject", "from", "to", "body");
        CommandSendMessage expected = new CommandSendMessage(mes);

        String serializedCommand = expected.serializeToStr();

        CommandSendMessage com = (CommandSendMessage) Command.deserializeFromStr(serializedCommand);
        assertEquals(expected, com);
    }

}
