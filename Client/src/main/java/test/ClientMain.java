package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

import io.github.cdimascio.dotenv.Dotenv;

public class ClientMain {

    private static int Port;

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        Port = Integer.parseInt(Objects.requireNonNull(dotenv.get("PORT")));

        Scanner in = new Scanner(System.in);
        System.out.println("Input login then password");
        String login = in.nextLine();
        String password = in.nextLine();
        Authorization auth = new Authorization(login, password);

        try (Socket sock = new Socket("localhost", Port)) {
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
            outputStream.writeUTF(auth.serializeToString());
            outputStream.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
