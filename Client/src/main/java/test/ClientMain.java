package test;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

import command_models.Authorization;
import commands.CommandAuthorization;
import io.github.cdimascio.dotenv.Dotenv;

import static java.lang.Thread.sleep;


public class ClientMain {

    private static int PORT;

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        PORT = Integer.parseInt(Objects.requireNonNull(dotenv.get("PORT")));

        System.out.println("Input login then password");

        Scanner in = new Scanner(System.in);
        String login = in.nextLine();
        String password = in.nextLine();

        Authorization auth = new Authorization(login, password);

        try (Socket socket = new Socket("localhost", PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             /*PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)*/){

            //Scanner scanner = new Scanner(System.in);

            // Поток для чтения сообщений от сервера
            new Thread(() -> {
                try {
                    String serverMessage;
                    while(true) {
                        if ((serverMessage = reader.readLine()) != null) {
                            System.out.println(serverMessage);
                        }
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }).start();
            sendAuthorization(socket,new CommandAuthorization(auth));
            while(true) {

            }

        } catch (IOException e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        }


    }
    static void sendAuthorization(Socket socket, CommandAuthorization auth) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(auth.serializeToStr() + "\n"); // Добавляем перенос строки
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
