package test;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

import command_models.Authorization;
import io.github.cdimascio.dotenv.Dotenv;

public class ClientMain {

    private static int PORT;

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        PORT = Integer.parseInt(Objects.requireNonNull(dotenv.get("PORT")));

        Scanner in = new Scanner(System.in);
        System.out.println("Input login then password");
        String login = in.nextLine();
        String password = in.nextLine();
        Authorization auth = new Authorization(login, password);

        try (Socket sock = new Socket("localhost", PORT)) {
            System.err.println("initialized");
            sendAuthorization(sock, auth);
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            System.err.println("bye...");
        }
    }

    static void sendAuthorization(Socket sock, Authorization auth) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(sock.getOutputStream()))
        {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(auth.serializeToStr());
            writer.flush();
            outputStream.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
