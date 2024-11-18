package database;

import java.sql.*;
import java.util.Objects;

import command_models.Authorization;
import io.github.cdimascio.dotenv.Dotenv;
import org.h2.security.SHA256;
import tools.Sha256;

import static java.lang.System.*;

public class DataBase {
    private static Connection con;
    private static Statement st;
    public static void main(String[] args) throws SQLException {
        connectToDataBase();
        try {
            add(new Authorization("Senya", "1111"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public static void add(Authorization auth) throws SQLException {

        st = con.createStatement();
        String login = auth.getLogin(); // Замените на значение login
        String hash = Sha256.hash(auth.getPassword());
        String req = "INSERT INTO USERS(NAME, HASHED_PASSWORD) VALUES (" + "'" + login+"'" + ",   '" + hash+"' )";// Замените на значение password
        try {
            st.execute(req);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());

        }
    }
}
