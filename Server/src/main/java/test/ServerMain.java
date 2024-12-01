package test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import command_models.Authorization;
import command_models.Message;
import command_models.Registration;
import commands.*;
import models.User;
import requests.RequestAuthorization;
import requests.RequestGetMessage;
import requests.RequestRegistration;
import requests.RequestSendMessage;
import tools.Env;
import tools.JWT_TOKEN;
import tools.Sha256;

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
            while(true) {
                Socket socket = serverSocket.accept();

                new Thread(new Handler(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при работе сервера" + e);
        }
    }
}





class Handler implements Runnable {

    BufferedWriter writer;
    Socket socket;
    String login;

    public Handler(Socket socket) {
        this.socket = socket;
        open_writer();
    }

    public final void open_writer() {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void sendRequest(String req) {
        try {
            writer.write(req + "\n");
            writer.flush();
            System.out.println("Sent: " + req);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleAuthorization(Authorization auth) {
        try {
            User user = getUser(auth.getLogin());
            if(user == null) {
                //Login incorrect
                sendRequest(new RequestAuthorization(false, " ").serializeToStr());
            }
            else {
                if(user.getPassword().equals(Sha256.hash(auth.getPassword()))) {
                    String token = JWT_TOKEN.createJwt(auth.getLogin());
                    sendRequest(new RequestAuthorization(true, token).serializeToStr());
                } else {
                    //Wrong password
                    sendRequest(new RequestAuthorization(false, " ").serializeToStr());
                }
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void handleRegistration(Registration reg) {
        try {
            User user = getUser(reg.getLogin());

            if(user != null) {
                sendRequest(new RequestRegistration(false).serializeToStr());
            }
            else {
              addUser(reg);
              this.login = reg.getLogin();
              sendRequest(new RequestRegistration(true).serializeToStr());
            }
        }
        catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void handleSendMessage(Message msg) {
        // TODO
    }

    public void handleGetMessage(CommandGetMessage msg) {
        // TODO
    }

    private Boolean checkJWT(String token) {
        return JWT_TOKEN.validateJwt(token);
    }

    @Override
    public void run() {
        try (
                InputStream input = socket.getInputStream();
                //OutputStream output = socket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        ) {
            //writer.println("Добро пожаловать на сервер!");
            String message;

            while ((message = reader.readLine()) != null) {

                if (message.equalsIgnoreCase("exit")) {
                    sendRequest("Прощайте!");
                    break;
                }

                System.out.println(message);
                //writer.println("Сервер получил: " + message);

                Command command = Command.deserializeFromStr(message);

                switch(command.getType()) {
                    case CommandType.LOGIN -> {
                        Authorization auth = ((CommandAuthorization) command).getAuthorization();
                        handleAuthorization(auth);
                    }
                    case CommandType.REGISTER -> {
                        Registration reg = ((CommandRegistration) command).getRegistration();
                        handleRegistration(reg);
                    }
                    case CommandType.SEND_MESSAGE -> {
                        if(checkJWT(command.getJwtToken())) {
                            Message mes = ((CommandSendMessage) command).getMessage();
                            handleSendMessage(mes);
                        } else {
                            sendRequest(new RequestSendMessage(false).serializeToStr());
                        }
                    }
                    case CommandType.GET_MESSAGE -> {
                        if(checkJWT(command.getJwtToken())) {
                            handleGetMessage((CommandGetMessage) command);
                        }
                        else {
                            sendRequest(new RequestGetMessage(new ArrayList<Message>()).serializeToStr());
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при общении с клиентом: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }
}
