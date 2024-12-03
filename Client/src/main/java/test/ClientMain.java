package test;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import command_models.Authorization;
import command_models.Message;
import command_models.Registration;
import commands.CommandAuthorization;
import commands.CommandRegistration;
import commands.CommandSendMessage;
import commands.CommandType;
import io.github.cdimascio.dotenv.Dotenv;
import requests.*;
import requests.RequestType;

import static java.lang.Thread.sleep;


public class ClientMain {

    private static int PORT;
    private static String jwt_token;

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        PORT = Integer.parseInt(Objects.requireNonNull(dotenv.get("PORT")));

        System.out.println("Input login then password to register");

        Scanner in = new Scanner(System.in);
        String login = in.nextLine();
        String password = in.nextLine();

        Registration reg = new Registration(login,password);
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
            if(in.nextLine().equals("reg")){
                sendRegistration(socket,new CommandRegistration(reg));
            }
            else sendAuthorization(socket,new CommandAuthorization(auth));

            System.out.println("input receiver, subject, and the body of the message");
            String receiver = in.nextLine();
            String subject = in.nextLine();
            String body = in.nextLine();
            Message mes = new Message(subject,login,receiver,body);
            sendMessage(socket, new CommandSendMessage(mes,jwt_token));
            //try {sleep(500);} catch (InterruptedException e) {System.err.println(e.getMessage());}

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

    static void sendRegistration(Socket socket, CommandRegistration reg) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(reg.serializeToStr() + "\n"); // Добавляем перенос строки
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void sendMessage(Socket socket, CommandSendMessage message) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(message.serializeToStr() + "\n"); // Добавляем перенос строки
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void HandleRequest(String message) throws Exception {
        Request req = Request.deserializeFromStr(message);

        switch(req.getType()) {
            case RequestType.LOGIN -> {
                RequestAuthorization authRequest = (RequestAuthorization) req;
                if (authRequest.isAuthorized()){
                    System.out.println("Authorized successfully\n");
                    jwt_token = authRequest.getJwt_token();
                    System.out.println(jwt_token);
                }
                else System.out.println("Incorrect login or password\n");
            }
            case RequestType.REGISTER -> {
                RequestRegistration regRequest = (RequestRegistration) req;
                if (regRequest.isRegistered()){
                    System.out.println("Registered successfully\n");
                }
                else System.out.println("Login already exists\n");
            }
            case RequestType.SEND_MESSAGE -> {
                RequestSendMessage sendRequest = (RequestSendMessage) req;
                if (sendRequest.isSent()){
                    System.out.println("Your message had been sent\n");
                }
                else System.out.println("Your message hadn't been sent, user with this nickname probably dont exists\n");
            }
            case RequestType.GET_MESSAGE -> {
                ArrayList<Message> mes = ((RequestGetMessage) req).getMessages();
                if (mes.size() == 0) System.out.println("No new messages for you right now\n");
                for (int i = 0; i<mes.size();i++){
                    System.out.println(mes.get(i).serializeToStr());
                }
            }
        }
        System.out.println();
    }
}
