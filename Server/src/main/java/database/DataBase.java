package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

import command_models.Message;
import command_models.Registration;
import models.User;
import tools.Env;
import tools.Sha256;

public class DataBase {
    private static Connection con;
    public static void connectToDataBase() throws SQLException {

        String URL = Env.getURL();
        String USERNAME = Env.getUsername();
        String PASSWORD = Env.getPassword();
        try {
            con = DriverManager.getConnection(URL,USERNAME,PASSWORD );
            String req = "CREATE TABLE IF NOT EXISTS `USERS` (" +
                    "    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "    `NAME` varchar(30)," +
                    "    `HASHED_PASSWORD` varchar(256)" +
                    ");";
            String messageTableReq =
                    "CREATE TABLE IF NOT EXISTS messages (" +
                            "    id INT AUTO_INCREMENT PRIMARY KEY," +
                            "    sender_id INT NOT NULL," +
                            "    receiver_id INT NOT NULL," +
                            "    subject VARCHAR(255)," +
                            "    body TEXT," +
                            "    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                            "    FOREIGN KEY (sender_id) REFERENCES users(id)," +
                            "    FOREIGN KEY (receiver_id) REFERENCES users(id)" +
                            ");";

            Statement state = con.createStatement();
           state.execute(messageTableReq);
           state.execute(req);
           state.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addUser(Registration reg) throws SQLException {

        String login = reg.getLogin();
        String hash = Sha256.hash(reg.getPassword());
        String req = "INSERT INTO USERS(NAME, HASHED_PASSWORD) VALUES(?, ?);";

        PreparedStatement preparedStatement = con.prepareStatement(req);

        preparedStatement.setString(1, login);
        preparedStatement.setString(2, hash);

        int amount = preparedStatement.executeUpdate();

        if (amount > 0) {
            System.out.println("User added: " + login);
        }
        else {
            System.out.println("User not found.");
        }
    }

    public static User getUser(String login) throws SQLException {

        String req = "SELECT * FROM USERS WHERE NAME = ?";

        PreparedStatement preparedStatement = con.prepareStatement(req);

        preparedStatement.setString(1, login);

        ResultSet rs = preparedStatement.executeQuery();

        if(rs.next()) {
            return new User(rs.getString("name"), rs.getString("hashed_password"));
        }
        else {
            return null;
        }
    }

    public static int getId(String login) throws SQLException {
        String req =  "SELECT id FROM USERS WHERE NAME = ?";
        PreparedStatement preparedStatement = con.prepareStatement(req);

        preparedStatement.setString(1, login);

        ResultSet rs = preparedStatement.executeQuery();

        if(rs.next()) {
            return rs.getInt("id");
        }
        else {
            return -1;
        }
    }

    public static void addMessage(Message mess) throws SQLException{
        int from = getId(mess.getFrom());
        int to = getId(mess.getTo());
        if (to == -1){
            System.out.println("Message from "+mess.getFrom()+" to "+mess.getTo()+" wasn't sent because receiver doen't exists");
        }
        String subject = mess.getSubject();
        String body = mess.getBody();
        String req = "INSERT INTO messages(sender_id, receiver_id, subject, body) VALUES (?, ?, ?, ?);";

        PreparedStatement preparedStatement = con.prepareStatement(req);

        preparedStatement.setInt(1, from);
        preparedStatement.setInt(2, to);
        preparedStatement.setString(3,subject);
        preparedStatement.setString(4,body);

        int amount = preparedStatement.executeUpdate();

        if (amount > 0) {
            System.out.println("Message from "+mess.getFrom()+" to "+mess.getTo()+" was sent");
        }
        else {
            System.out.println("Message from "+mess.getFrom()+" to "+mess.getTo()+" wasn't sent because of database errors");
        }
    }

    public static ArrayList<Message> getMessages(String login) throws SQLException {
        System.out.println("getting messages from db");
        int receiver_id = getId(login);
        System.out.println("hini's id " + Integer.toString(receiver_id));
        String req = "SELECT * FROM messages WHERE receiver_id = ?";

        PreparedStatement preparedStatement = con.prepareStatement(req);
        preparedStatement.setInt(1, receiver_id);
        ResultSet rs = preparedStatement.executeQuery();

        ArrayList<Message> messages = new ArrayList<>();
        while (rs.next()) {

            int sender_id = rs.getInt("sender_id");
            String from = "id - " + sender_id;

            String subject = rs.getString("subject");
            String body = rs.getString("body");

            messages.add(new Message(subject, from, login, body));
        }
        if (messages.size()>0) return messages;
        else {
            System.out.println("No messages for u");
            return null;
        }
    }
}
