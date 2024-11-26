package test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Objects;

import command_models.Authorization;
import command_models.Message;
import command_models.Registration;
import commands.*;
import io.github.cdimascio.dotenv.Dotenv;
import models.User;
import tools.Sha256;

import static database.DataBase.*;

public class ServerMain {
    private static int PORT;

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        try {
            connectToDataBase();
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        PORT = Integer.parseInt(Objects.requireNonNull(dotenv.get("PORT")));

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

    public Handler(Socket socket) {
        this.socket = socket;
        open_writer();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
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
            writer.write(req);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleAuthorization(Authorization auth) {
        try {
            User user = getUser(auth.getLogin());
            if(user == null) {
                sendRequest("login not exists!");
            }
            else {
                if(user.getPassword().equals(Sha256.hash(auth.getPassword()))) {
                    sendRequest("login success!");
                } else {
                    sendRequest("login failed!");
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
                sendRequest("Login already exists!");
            } else {
              addUser(reg);
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void handleSendMessage(Message msg) {
        // TODO
    }

    public void handleGetMessage(Message msg) {
        // TODO
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
                        Message mes = ((CommandSendMessage) command).getMessage();
                        handleSendMessage(mes);
                    }
                    case CommandType.GET_MESSAGE -> {
                        Message mes = ((CommandSendMessage) command).getMessage();
                        handleGetMessage(mes);
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
