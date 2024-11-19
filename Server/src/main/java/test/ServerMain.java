package test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

import io.github.cdimascio.dotenv.Dotenv;

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
