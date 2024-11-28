package database;

import java.sql.*;
import java.util.Objects;

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
