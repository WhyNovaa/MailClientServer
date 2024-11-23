package database;

import java.sql.*;
import java.util.Objects;

import command_models.Authorization;
import command_models.Registration;
import io.github.cdimascio.dotenv.Dotenv;
import models.User;
import tools.Sha256;


public class DataBase {
    private static Connection con;

    public static void main(String[] args) throws SQLException {
        connectToDataBase();
        /*try {
            //add(new Authorization("Senya", "1111"));
            String login = "Misha";  // Значения получены от пользователя
            User u = getUser(login);

            if(u != null) {
                System.out.println(u);
            } else {
                System.out.println("User not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
        String aboba = "test";

        System.out.println(Sha256.hash(aboba));
    }

    public static void connectToDataBase() throws SQLException {

        Dotenv dotenv = Dotenv.load();
        String URL = Objects.requireNonNull(dotenv.get("URL"));
        String USER = Objects.requireNonNull(dotenv.get("USER"));
        String PASSWORD = Objects.requireNonNull(dotenv.get("PASSWORD"));

        try {
            con = DriverManager.getConnection(URL,USER,PASSWORD );
            String req = "CREATE TABLE IF NOT EXISTS `USERS` (" +
                    "    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "    `NAME` varchar(30)," +
                    "    `HASHED_PASSWORD` varchar(256)" +
                    ")";

           Statement state = con.createStatement();
           state.execute(req);
           state.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addUser(Registration reg) throws SQLException {

        String login = reg.getLogin(); // Замените на значение login
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
}
