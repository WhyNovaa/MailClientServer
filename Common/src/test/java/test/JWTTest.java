package test;

import org.junit.jupiter.api.Test;
import tools.JWT_TOKEN;

import static org.junit.jupiter.api.Assertions.*;

public class JWTTest {
    @Test
    public void isValidCreateJWT() {
        String token = JWT_TOKEN.createJwt("test");
        assertNotEquals("", token);
    }

    @Test
    public void isValidVerifyJWT() {
        String token = JWT_TOKEN.createJwt("test");
        assertTrue(JWT_TOKEN.validateJwt(token));

        String fake_token = "test";
        assertFalse(JWT_TOKEN.validateJwt(fake_token));
    }
}
