package test;

import command_models.*;
import commands.*;
import io.github.cdimascio.dotenv.Dotenv;
import requests.*;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static test.FileUtil.decodeBase64ToFile;
import static test.FileUtil.encodeFileToBase64;

public class ClientMain {

    private static int PORT;
    private static String jwt_token = null;
    private static String DIRECTORY;
    private static volatile boolean running = true;
    private static volatile boolean waiting = true;

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        PORT = Integer.parseInt(Objects.requireNonNull(dotenv.get("PORT")));
        DIRECTORY = Objects.requireNonNull(dotenv.get("DIRECTORY"));
        createDirectoryIfNeeded(DIRECTORY);

        Scanner in = new Scanner(System.in);

        try (Socket socket = new Socket("localhost", PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            Thread readerThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while (running) {
                        StringBuilder xmlMessage = new StringBuilder();
                        String line;

                        // Считываем сообщение до конца
                        while ((line = reader.readLine()) != null) {
                            if (line.trim().isEmpty()) {
                                break;
                            }
                            xmlMessage.append(line).append("\n");
                        }

                        System.out.println(xmlMessage);
                        if (xmlMessage.length() == 0) {
                            // Нет новых сообщений, продолжаем ожидать
                            continue;
                        }
                        HandleRequest(xmlMessage.toString());
                        waiting = false;

                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            readerThread.start();

            String login = "";
            String password = "";

            while (jwt_token == null) {
                System.out.println("Input reg to register or log to log in");

                switch (in.nextLine()) {
                    case "reg" -> {
                        System.out.println("Input login then password");
                        login = in.nextLine();
                        password = in.nextLine();

                        Registration reg = new Registration(login, password);
                        sendCommand(writer, new CommandRegistration(reg));
                        waiting = true;
                        while (waiting) {}
                    }
                    case "log" -> {
                        System.out.println("Input login then password");
                        login = in.nextLine();
                        password = in.nextLine();

                        Authorization auth = new Authorization(login, password);
                        sendCommand(writer, new CommandAuthorization(auth));
                        waiting = true;
                        while (waiting) {}
                    }
                    default -> System.out.println("Wrong input");
                }
            }

            System.out.println("Input write to write message, get to read your messages, file to send file, exit to exit");
            String answer = in.nextLine().trim();
            while (!answer.equals("exit") && jwt_token != null) {
                switch (answer) {
                    case "write" -> {
                        System.out.println("Input receiver, subject, and the body of the message");
                        String receiver = in.nextLine();
                        String subject = in.nextLine();
                        String body = in.nextLine();
                        Message mes = new Message(subject, login, receiver, body);
                        sendCommand(writer, new CommandSendMessage(mes, jwt_token));
                        waiting = true;
                        while (waiting) {}
                    }
                    case "get" -> {
                        sendCommand(writer, new CommandGetMessage(jwt_token));
                        waiting = true;
                        while (waiting) {}
                    }
                    case "file" -> {
                        System.out.println("Input receiver and path to file");
                        String receiver = in.nextLine();
                        String path = in.nextLine();
                        MessageFileWrapper file = encodeFileToBase64(path, login, receiver);
                        sendCommand(writer, new CommandSendFile(file, jwt_token));
                        waiting = true;
                        while (waiting) {}
                    }
                    default -> System.out.println("Wrong input");
                }
                System.out.println("Input write to write message, get to read your messages, file to send file, exit to exit");
                answer = in.nextLine().trim();
            }
            running = false;
            readerThread.interrupt();
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        }
    }

    private static void createDirectoryIfNeeded(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created successfully: " + directory);
            } else {
                System.out.println("Failed to create the directory: " + directory);
            }
        } else {
            System.out.println("Directory already exists: " + directory);
        }
    }

    static void sendCommand(BufferedWriter writer, Command command) {
        try {
            String xml = XMLUtils.objectToXML(command);
            writer.write(xml + "\n");
            writer.flush();
        } catch (IOException | JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    static void HandleRequest(String message) throws Exception {
        System.out.println(message);
        Request req = (Request) XMLUtils.xmlToObject(message, Request.class);

        switch (req.getType()) {
            case RequestType.LOGIN -> {
                RequestAuthorization authRequest = (RequestAuthorization) req;
                if (authRequest.isAuthorized()) {
                    System.out.print("Authorized successfully");
                    jwt_token = authRequest.getJwt_token();
                } else {
                    System.out.print("Incorrect login or password");
                }
            }
            case RequestType.REGISTER -> {
                RequestRegistration regRequest = (RequestRegistration) req;
                if (regRequest.isRegistered()) {
                    System.out.print("Registered successfully");
                } else {
                    System.out.print("Login already exists");
                }
            }
            case RequestType.SEND_MESSAGE -> {
                RequestSendMessage sendRequest = (RequestSendMessage) req;
                if (sendRequest.isSent()) {
                    System.out.print("Your message had been sent");
                } else {
                    System.out.print("Your message hadn't been sent, user with this nickname probably doesn't exist");
                }
            }
            case RequestType.GET_MESSAGE -> {
                //RequestGetMessage getreq = ((RequestGetMessage) req);
                ArrayList<Message> mes = (ArrayList<Message>) ((RequestGetMessage) req).getMessages();
                if (mes == null || mes.isEmpty()) {
                    System.out.println("No new messages for you right now");
                } else {
                    for (Message ur_message : mes) {
                        System.out.println("from: " + ur_message.getFrom());
                        System.out.println("subject: " + ur_message.getSubject());
                        System.out.print("text: " + ur_message.getBody());
                    }
                }
            }
            case RequestType.GET_FILE -> {
                ArrayList<MessageFileWrapper> files = (ArrayList<MessageFileWrapper>) ((RequestGetFile) req).getFiles();
                if (files == null || files.isEmpty()) {
                    System.out.println("No new files for you right now");
                } else {
                    for (MessageFileWrapper file : files) {
                        decodeBase64ToFile(file, DIRECTORY);
                    }
                }
            }
        }
        System.out.println();
    }
}
