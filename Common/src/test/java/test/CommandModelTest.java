package test;

import command_models.FileWrapper;
import command_models.Message;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import command_models.Authorization;
import tools.Separator;

public class CommandModelTest {
    @Test
    public void isValidAuthorizationSerialization() {
        Authorization auth = new Authorization("Misha", "1111");
        String expected = "Misha" + Separator.SEPARATOR + "1111";
        assertEquals(expected, auth.serializeToStr());
    }

    @Test
    public void isValidRegistrationSerialization() {
        Authorization reg = new Authorization("Misha", "1111");
        String expected = "Misha" + Separator.SEPARATOR + "1111";
        assertEquals(expected, reg.serializeToStr());
    }

    @Test
    public void isValidMessageSerialization() {
        Message mes = new Message("subject", "from", "to", "body");
        String expected = "subject" + Separator.SEPARATOR + "from" + Separator.SEPARATOR + "to" + Separator.SEPARATOR + "body";
        assertEquals(expected, mes.serializeToStr());
    }

    @Test
    public void isValidFileSerialization() {
        FileWrapper file = new FileWrapper("subject", "from", "to", "body");
        String expected = "subject" + Separator.SEPARATOR + "from" + Separator.SEPARATOR + "to" + Separator.SEPARATOR + "body";
        assertEquals(expected, file.serializeToStr());
    }
}
