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
        String expected = "LOGIN" + Separator.SEPARATOR + "admin" + Separator.SEPARATOR + "admin" + Separator.SEPARATOR + " ";
        assertEquals(expected, commandAuthorization.serializeToStr());
    }

    @Test
    public void isValidCommandRegistrationSerialization() {
        Registration reg = new Registration("admin", "admin");
        CommandRegistration commandRegistration = new CommandRegistration(reg);
        String expected = "REGISTER" + Separator.SEPARATOR + "admin" + Separator.SEPARATOR + "admin" + Separator.SEPARATOR + " ";
        assertEquals(expected, commandRegistration.serializeToStr());
    }

    @Test
    public void isValidCommandGetMessageSerialization() {
        CommandGetMessage commandGetMessage = new CommandGetMessage("JWT");
        String expected = "GET_MESSAGE:JWT";
        assertEquals(expected, commandGetMessage.serializeToStr());
    }

    @Test
    public void isValidCommandSendMessageSerialization() {
        Message mes = new Message("subject", "from", "to", "body");
        CommandSendMessage commandSendMessage = new CommandSendMessage(mes, "JWT");
        String expected = "SEND_MESSAGE" + Separator.SEPARATOR + "subject" + Separator.SEPARATOR + "from" + Separator.SEPARATOR + "to" + Separator.SEPARATOR + "body" + Separator.SEPARATOR + "JWT";
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
        CommandGetMessage expected = new CommandGetMessage("JWT");

        String serializedCommand = expected.serializeToStr();

        CommandGetMessage com = (CommandGetMessage) Command.deserializeFromStr(serializedCommand);
        assertEquals(expected, com);
    }

    @Test
    public void isValidCommandDeserializationToCommandSendMessage() {
        //CommandSendMessage
        Message mes = new Message("subject", "from", "to", "body");
        CommandSendMessage expected = new CommandSendMessage(mes, "JWT");

        String serializedCommand = expected.serializeToStr();

        CommandSendMessage com = (CommandSendMessage) Command.deserializeFromStr(serializedCommand);
        assertEquals(expected, com);
    }

}
