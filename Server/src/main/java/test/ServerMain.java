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

import static database.DataBase.addUser;
import static database.DataBase.getUser;

public class ServerMain {
    private static int PORT;

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
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
                    case CommandType.REGISTER -> {} // TODO
                    case CommandType.SEND_MESSAGE -> {} // TODO
                    case CommandType.GET_MESSAGE -> {} // TODO
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
