package test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Objects;

import command_models.Authorization;
import command_models.Registration;
import commands.Command;
import commands.CommandAuthorization;
import commands.CommandRegistration;
import commands.CommandType;
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
            System.err.println(e);
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
    Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    static void sendRequest(Socket sock, String req) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
            writer.write(req);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleAuthorization(Socket socket, Authorization auth) {
        try {
            User user = getUser(auth.getLogin());
            if(user == null) {
                sendRequest(socket, "login not exists!");
            }
            else {
                if(user.getPassword().equals(Sha256.hash(auth.getPassword()))) {
                    sendRequest(socket, "login success!");
                } else {
                    sendRequest(socket, "login failed!");
                }
            }
        } catch(SQLException e) {
            System.err.println(e);
        }
    }

    public void handleRegistration(Socket socket, Registration reg) {
        try {
            User user = getUser(reg.getLogin());
            if(user != null) {
                sendRequest(socket, "Login already exists!");
            } else {
              addUser(reg);
            }
        } catch(SQLException e) {
            System.err.println(e);
        }
    }

    public void handleSendMessage(Socket socket, String msg) {
        // TODO
    }

    public void handleGetMessage(Socket socket, String msg) {
        // TODO
    }
    @Override
    public void run() {
        try (
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                PrintWriter writer = new PrintWriter(output, true)
        ) {
            writer.println("Добро пожаловать на сервер!");
            String message;

            while ((message = reader.readLine()) != null) {

                System.out.println(message);
                writer.println("Сервер получил: " + message);

                Command command = Command.deserializeFromStr(message);

                switch(command.getType()) {
                    case CommandType.LOGIN -> {
                        Authorization auth = ((CommandAuthorization) command).getAuthorization();
                        handleAuthorization(socket, auth);
                    }
                    case CommandType.REGISTER -> {
                        Registration reg = ((CommandRegistration) command).getRegistration();
                        handleRegistration(socket, reg);
                    }
                    case CommandType.SEND_MESSAGE -> {
                        handleSendMessage(socket, message);
                    }
                    case CommandType.GET_MESSAGE -> {
                        handleGetMessage(socket, message);
                    }
                }

                if (message.equalsIgnoreCase("exit")) {
                    writer.println("Прощайте!");
                    break;
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
