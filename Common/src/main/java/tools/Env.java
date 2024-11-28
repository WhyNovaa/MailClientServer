package tools;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;

public class Env {
    private static final boolean isTest = Boolean.parseBoolean(System.getProperty("IS_TEST", "false"));
    private static final String dotenvPath = isTest ? "../" : "./";

    private static final Dotenv dotenv = Dotenv.configure()
            .directory(dotenvPath)
            .filename(".env")
            .load();


    public static String getSecretKey() {
        String secretKey = dotenv.get("SECRET_KEY");
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("SECRET_KEY is not set in the environment variables");
        }
        return secretKey;
    }

    public static long getTokenExpiration() {
        return Long.parseLong(Objects.requireNonNull(dotenv.get("TOKEN_EXPIRATION"))) * 1000L;
    }

    public static int getPort() {
        return Integer.parseInt(Objects.requireNonNull(dotenv.get("PORT")));
    }

    public static String getURL() {
        return Objects.requireNonNull(dotenv.get("URL"));
    }

    public static String getUsername() {
        return Objects.requireNonNull(dotenv.get("LOGIN_USERNAME"));
    }

    public static String getPassword() {
        return Objects.requireNonNull(dotenv.get("PASSWORD"));
    }
}
