package test;

import command_models.Authorization;
import command_models.MessageFileWrapper;
import command_models.Message;
import command_models.Registration;
import commands.*;
import io.github.cdimascio.dotenv.Dotenv;
import requests.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static test.FileUtil.decodeBase64ToFile;
import static test.FileUtil.encodeFileToBase64;


public class ClientMain {

    private static int PORT;
    private static String jwt_token;
    private static String DIRECTORY;

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        PORT = Integer.parseInt(Objects.requireNonNull(dotenv.get("PORT")));
        DIRECTORY = Objects.requireNonNull(dotenv.get("DIRECTORY"));

        System.out.println("Input login then password to register or authorize");

        Scanner in = new Scanner(System.in);
        String login = in.nextLine();
        String password = in.nextLine();

        Registration reg = new Registration(login, password);
        Authorization auth = new Authorization(login, password);

        try (Socket socket = new Socket("localhost", PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ) {

            new Thread(() -> {
                try {
                    String serverMessage;
                    while (true) {
                        if ((serverMessage = reader.readLine()) != null) {
                            HandleRequest(serverMessage);
                        }
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();

            System.out.println("Input reg to register else anything");
            if (in.nextLine().equals("reg")) {
                sendRegistration(socket, new CommandRegistration(reg));
            } else sendAuthorization(socket, new CommandAuthorization(auth));

            System.out.println("Input write to write message, get to read ur messages");
            String answer = in.nextLine();
            while (!answer.equals("exit") && jwt_token!=null) {

                switch (answer) {

                    case "write" -> {
                        System.out.println("input receiver, subject, and the body of the message");
                        String receiver = in.nextLine();
                        String subject = in.nextLine();
                        String body = in.nextLine();
                        Message mes = new Message(subject, login, receiver, body);
                        sendMessage(socket, new CommandSendMessage(mes, jwt_token));
                    }

                    case "get" -> {
                        sendMessagesCheck(socket, new CommandGetMessage(jwt_token));
                    }

                    case "file" -> {
                        System.out.println("input receiver and path to file");
                        String receiver = in.nextLine();
                        String path = in.nextLine();
                        MessageFileWrapper file = encodeFileToBase64(path, login, receiver);
                        sendFile(socket, new CommandSendFile(file, jwt_token));
                    }

                    default -> {
                        System.out.println("wrong input");
                    }
                }

                System.out.println("Input write to write message, get to read ur messages, exit to exit");
                answer = in.nextLine();
            }

        } catch (IOException e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        }


    }


    static void sendAuthorization(Socket socket, CommandAuthorization auth) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(auth.serializeToStr() + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void sendRegistration(Socket socket, CommandRegistration reg) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(reg.serializeToStr() + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void sendMessage(Socket socket, CommandSendMessage message) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(message.serializeToStr() + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void sendFile(Socket socket, CommandSendFile file) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(file.serializeToStr() + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void sendMessagesCheck(Socket socket, CommandGetMessage message) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(message.serializeToStr() + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void HandleRequest(String message) throws Exception {
        Request req = Request.deserializeFromStr(message);

        switch (req.getType()) {
            case RequestType.LOGIN -> {
                RequestAuthorization authRequest = (RequestAuthorization) req;
                if (authRequest.isAuthorized()) {
                    System.out.println("Authorized successfully");
                    jwt_token = authRequest.getJwt_token();
                    //System.out.println(jwt_token);
                } else System.out.println("Incorrect login or password\n");
            }
            case RequestType.REGISTER -> {
                RequestRegistration regRequest = (RequestRegistration) req;
                if (regRequest.isRegistered()) {
                    System.out.println("Registered successfully\n");
                } else System.out.println("Login already exists\n");
            }
            case RequestType.SEND_MESSAGE -> {
                RequestSendMessage sendRequest = (RequestSendMessage) req;
                if (sendRequest.isSent()) {
                    System.out.println("Your message had been sent\n");
                } else
                    System.out.println("Your message hadn't been sent, user with this nickname probably doesn`t exist\n");
            }
            case RequestType.GET_MESSAGE -> {
                ArrayList<Message> mes = ((RequestGetMessage) req).getMessages();
                if (mes.isEmpty()) System.out.println("No new messages for you right now");
                for (Message ur_message : mes) {
                    System.out.println("from: " + ur_message.getFrom());
                    System.out.println("subject: " + ur_message.getSubject());
                    System.out.println("text: " + ur_message.getBody());
                }
            }
            case RequestType.GET_FILE -> {
                ArrayList<MessageFileWrapper> files = ((RequestGetFile) req).getFiles();
                if (files.isEmpty()) System.out.println("No new files for you right now");
                for (MessageFileWrapper file : files) {
                    decodeBase64ToFile(file, DIRECTORY);
                }
            }
        }
        System.out.println();
    }
}
