package test;

import org.junit.jupiter.api.Test;
import tools.Sha256;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Sha256Test {
    @Test
    public void isValidSha256() {
        String str = "test";
        String expected = "36f028580bb02cc8272a9a020f4200e346e276ae664e45ee80745574e2f5ab80";
        assertEquals(expected, Sha256.hash(str));
    }
}
