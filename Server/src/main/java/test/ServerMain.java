package test;

import command_models.Authorization;
import command_models.MessageFileWrapper;
import command_models.Message;
import command_models.Registration;
import commands.*;
import models.User;
import org.xml.sax.SAXException;
import requests.*;
import tools.Env;
import tools.JWT_TOKEN;
import tools.Sha256;
import command_models.XMLUtils;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import static database.DataBase.*;

public class ServerMain {
    private static int PORT;

    public static void main(String[] args) {
        try {
            connectToDataBase();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        PORT = Env.getPort();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new Handler(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server Error" + e);
        }
    }
}


class Handler implements Runnable {

    BufferedWriter writer;
    Socket socket;
    String login;

    public Handler(Socket socket) {
        this.socket = socket;
        openWriter();
    }

    public final void openWriter() {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void sendRequest(Object req) {
        try {
            String xml = XMLUtils.objectToXML(req);
            writer.write(xml + "\n");
            writer.flush();
            System.out.println("Sent: " + xml);
        } catch (IOException | JAXBException e) {
            throw new RuntimeException(e);
        }
    }
    void sendUnusualRequest(String xml) {
        try {
            writer.write(xml + "\n");
            writer.flush();
            System.out.println("Sent: " + xml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleAuthorization(Authorization auth) {
        try {
            User user = getUser(auth.getLogin());
            if (user == null) {
                // Login incorrect
                sendRequest(new RequestAuthorization(false, " "));
            } else {
                if (user.getPassword().equals(Sha256.hash(auth.getPassword()))) {
                    String token = JWT_TOKEN.createJwt(auth.getLogin());
                    login = user.getLogin();
                    sendRequest(new RequestAuthorization(true, token));
                } else {
                    // Wrong password
                    sendRequest(new RequestAuthorization(false, " "));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void handleRegistration(Registration reg) {
        try {
            User user = getUser(reg.getLogin());
            if (user != null) {
                sendRequest(new RequestRegistration(false));
            } else {
                addUser(reg);
                this.login = reg.getLogin();
                sendRequest(new RequestRegistration(true));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void handleSendMessage(Message msg) throws SQLException {
        try {
            addMessage(msg);
            sendRequest(new RequestSendMessage(true));
        } catch (SQLException e) {
            sendRequest(new RequestSendMessage(false));
            throw new RuntimeException(e);
        }
    }

    public void handleSendFile(MessageFileWrapper file) throws SQLException {
        try {
            addFile(file);
            sendRequest(new RequestSendMessage(true));
        } catch (SQLException e) {
            sendRequest(new RequestSendMessage(false));
            throw new RuntimeException(e);
        }
    }

    public void handleGetMessage(CommandGetMessage msg) throws SQLException {
        System.out.println("sending request");
        ArrayList<Message> messages = getMessages(login);
        ArrayList<MessageFileWrapper> files = getFiles(login);
        if (messages.isEmpty()) {
            sendUnusualRequest("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <RequestGetMessage> <RequestType>GET_MESSAGE</RequestType>  </RequestGetMessage>");
        } else {
            sendRequest(new RequestGetMessage(getMessages(login)));
        }
        if(files.isEmpty()) {
            sendUnusualRequest("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <RequestGetFile> <RequestType>GET_FILE</RequestType>  </RequestGetFile>");
        } else {
            sendRequest(new RequestGetFile(files));
        }
    }

    private Boolean checkJWT(String token) {
        return JWT_TOKEN.validateJwt(token);
    }

    @Override
    public void run() {
        try (
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        ) {
            while (true) {
                StringBuilder xmlMessage = new StringBuilder();
                String line;

                // Считываем сообщение до конца
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        break;
                    }
                    xmlMessage.append(line).append("\n");
                }

                if (xmlMessage.length() == 0) {
                    // Нет новых сообщений, продолжаем ожидать
                    continue;
                }

                Command command = XMLUtils.xmlToObject(xmlMessage.toString(), Command.class);

                switch (command.getType()) {
                    case CommandType.LOGIN -> {
                        Authorization auth = ((CommandAuthorization) command).getAuthorization();
                        handleAuthorization(auth);
                    }
                    case CommandType.REGISTER -> {
                        Registration reg = ((CommandRegistration) command).getRegistration();
                        handleRegistration(reg);
                    }
                    case CommandType.SEND_MESSAGE -> {
                        if (checkJWT(command.getJwtToken())) {
                            Message mes = ((CommandSendMessage) command).getMessage();
                            handleSendMessage(mes);
                        } else {
                            sendRequest(new RequestSendMessage(false));
                        }
                    }
                    case CommandType.SEND_FILE -> {
                        if (checkJWT(command.getJwtToken())) {
                            MessageFileWrapper file = ((CommandSendFile) command).getFile();
                            handleSendFile(file);
                        } else {
                            sendRequest(new RequestSendMessage(false));
                        }
                    }
                    case CommandType.GET_MESSAGE -> {
                        if (checkJWT(command.getJwtToken())) {
                            handleGetMessage((CommandGetMessage) command);
                        } else {
                            System.out.println("wrong jwt");
                            sendRequest(new RequestGetMessage(new ArrayList<>()));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error in connection with client: " + e.getMessage());
        } catch (SQLException | JAXBException | SAXException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error while closing connection: " + e.getMessage());
            }
        }
    }
}
