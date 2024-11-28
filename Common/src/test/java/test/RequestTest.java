package test;

import command_models.Authorization;
import commands.Command;
import commands.CommandAuthorization;
import org.junit.jupiter.api.Test;
import requests.Request;
import requests.RequestAuthorization;
import tools.Separator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestTest {
    @Test
    public void isValidRequestAuthorizationSerialization() {

        RequestAuthorization reqtrue = new RequestAuthorization(true);
        String expectedTrue = "LOGIN" + Separator.SEPARATOR + reqtrue.getState();
        RequestAuthorization reqfalse = new RequestAuthorization(false);
        String expectedFalse ="LOGIN" + Separator.SEPARATOR + "not " + reqfalse.getState();
        assertEquals(expectedTrue, reqtrue.serializeToStr());
        assertEquals(expectedFalse, reqfalse.serializeToStr());
    }

    @Test
    public void isValidDeserializationToRequestAuthorization() throws Exception {
        RequestAuthorization expectedTrue = new RequestAuthorization(true);
        String serializedRequest = expectedTrue.serializeToStr();
        try {
            RequestAuthorization req = (RequestAuthorization) Request.deserializeFromStr(serializedRequest);
            assertEquals(expectedTrue, req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RequestAuthorization expectedFalse = new RequestAuthorization(false);
        String serializedRequest2 = expectedFalse.serializeToStr();
        try {
            RequestAuthorization req = (RequestAuthorization) Request.deserializeFromStr(serializedRequest2);
            assertEquals(expectedFalse, req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
