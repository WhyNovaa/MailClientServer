package test;

import org.junit.jupiter.api.Test;
import tools.Env;

import static org.junit.jupiter.api.Assertions.*;

public class EnvTest {
    @Test
    public void testSystemProperty() {
        String isTest = System.getProperty("IS_TEST", "false");
        System.out.println("IS_TEST: " + isTest);
        assertEquals("true", isTest);
    }
    @Test
    public void isValidGetSecretKey() {
        assertNotEquals("", Env.getSecretKey());
    }

    @Test
    public void isValidGetTokenExpiration() {
        assertTrue(Env.getTokenExpiration() > 0L);
    }

    @Test
    public void isValidGetPort() {
        assertTrue(Env.getPort() > 0);
    }

    @Test
    public void isValidGetURL() {
        assertNotEquals("", Env.getURL());
    }

    @Test
    public void isValidGetUsername() {
        assertNotEquals("", Env.getUsername());
    }

    @Test
    public void isValidGetPassword() {
        assertNotEquals("", Env.getPassword());
    }
}
