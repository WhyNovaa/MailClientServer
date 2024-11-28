package test;

import command_models.Message;
import commands.Command;
import commands.CommandSendMessage;
import org.junit.jupiter.api.Test;
import requests.Request;
import requests.*;
import tools.JWT;
import tools.Separator;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tools.JWT.createJwt;

public class RequestTest {

    @Test
    public void isValidRequestAuthorizationSerialization() {
        String token = createJwt("admin");
        RequestAuthorization reqtrue = new RequestAuthorization(true, token);
        String expectedTrue = "LOGIN" + Separator.SEPARATOR + reqtrue.getState() + Separator.SEPARATOR + token;
        RequestAuthorization reqfalse = new RequestAuthorization(false,token);
        String expectedFalse ="LOGIN" + Separator.SEPARATOR + "not " + reqfalse.getState()+Separator.SEPARATOR + token;
        assertEquals(expectedTrue, reqtrue.serializeToStr());
        assertEquals(expectedFalse, reqfalse.serializeToStr());
    }

    @Test
    public void isValidDeserializationToRequestAuthorization() throws Exception {
        String token = createJwt("admin");
        RequestAuthorization expectedTrue = new RequestAuthorization(true, token);
        String serializedRequest = expectedTrue.serializeToStr();
        try {
            RequestAuthorization req = (RequestAuthorization) Request.deserializeFromStr(serializedRequest);
            assertEquals(expectedTrue, req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        RequestAuthorization expectedFalse = new RequestAuthorization(false,token);
        String serializedRequest2 = expectedFalse.serializeToStr();
        try {
            RequestAuthorization req = (RequestAuthorization) Request.deserializeFromStr(serializedRequest2);
            assertEquals(expectedFalse, req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void isValidRequestRegistrationSerialization() {

        RequestRegistration reqtrue = new RequestRegistration(true);
        String expectedTrue = "REGISTER" + Separator.SEPARATOR + reqtrue.getState();
        RequestRegistration reqfalse = new RequestRegistration(false);
        String expectedFalse = "REGISTER" + Separator.SEPARATOR + "not " + reqfalse.getState();
        assertEquals(expectedTrue, reqtrue.serializeToStr());
        assertEquals(expectedFalse, reqfalse.serializeToStr());
    }

    @Test
    public void isValidDeserializationToRequestRegistration() throws Exception {
        RequestRegistration expectedTrue = new RequestRegistration(true);
        String serializedRequest = expectedTrue.serializeToStr();
        try {
            RequestRegistration req = (RequestRegistration) Request.deserializeFromStr(serializedRequest);
            assertEquals(expectedTrue, req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        RequestRegistration expectedFalse = new RequestRegistration(false);
        String serializedRequest2 = expectedFalse.serializeToStr();
        try {
            RequestRegistration req = (RequestRegistration) Request.deserializeFromStr(serializedRequest2);
            assertEquals(expectedFalse, req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void isValidRequestSendMessageSerialization() {

        RequestSendMessage reqtrue = new RequestSendMessage(true);
        String expectedTrue = "SEND_MESSAGE" + Separator.SEPARATOR + reqtrue.getState();
        RequestSendMessage reqfalse = new RequestSendMessage(false);
        String expectedFalse = "SEND_MESSAGE" + Separator.SEPARATOR + "not " + reqfalse.getState();
        assertEquals(expectedTrue, reqtrue.serializeToStr());
        assertEquals(expectedFalse, reqfalse.serializeToStr());
    }

    @Test
    public void isValidDeserializationToRequestSendMessage() throws Exception {
        RequestSendMessage expectedTrue = new RequestSendMessage(true);
        String serializedRequest = expectedTrue.serializeToStr();
        try {
            RequestSendMessage req = (RequestSendMessage) Request.deserializeFromStr(serializedRequest);
            assertEquals(expectedTrue, req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        RequestSendMessage expectedFalse = new RequestSendMessage(false);
        String serializedRequest2 = expectedFalse.serializeToStr();
        try {
            RequestSendMessage req = (RequestSendMessage) Request.deserializeFromStr(serializedRequest2);
            assertEquals(expectedFalse, req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void isValidRequestGetMessageSerialization() {
        Message mes1 = new Message("subject", "from", "to", "body");
        Message mes2 = new Message("theme", "admin", "user", "message");
        ArrayList<Message> messages = new ArrayList<Message>(2);
        messages.add(mes1);
        messages.add(mes2);

        RequestGetMessage reqtrue = new RequestGetMessage(messages);
        String expected = "GET_MESSAGE" + Separator.SEPARATOR + "subject" + Separator.SEPARATOR +
                "from" + Separator.SEPARATOR + "to" + Separator.SEPARATOR + "body" + Separator.SEPARATOR
                + "theme" + Separator.SEPARATOR +"admin" + Separator.SEPARATOR + "user" +Separator.SEPARATOR+ "message";
        assertEquals(expected, reqtrue.serializeToStr());
    }

    @Test
    public void isValidCommandDeserializationToRequestGetMessage() throws Exception {
        Message mes1 = new Message("subject", "from", "to", "body");
        Message mes2 = new Message("theme", "admin", "user", "message");
        ArrayList<Message> messages = new ArrayList<Message>(2);
        messages.add(mes1);
        messages.add(mes2);

        RequestGetMessage expected = new RequestGetMessage(messages);

        String serializedRequest = expected.serializeToStr();

        RequestGetMessage com = null;
        try {
            com = (RequestGetMessage) Request.deserializeFromStr(serializedRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(expected, com);
    }
}
