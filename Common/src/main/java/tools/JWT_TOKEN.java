package tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Date;

public class JWT_TOKEN {
    private static final Dotenv dotenv = Dotenv.configure().filename(".env").load();

    public static String createJwt(String subject) {
            Algorithm algorithm = Algorithm.HMAC256(getSecretKey().getBytes());

            return JWT.create()
                    .withSubject(subject)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // Время истечения (1 час)
                    .sign(algorithm);
    }

    public static boolean validateJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(getSecretKey());

            JWTVerifier verifier = JWT.require(algorithm).build();

            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    public static void main(String[] args) {
        String a = createJwt("test");
        System.out.println(a);
        System.out.println(validateJwt(a));
    }


    private static String getSecretKey() {
        String secretKey = dotenv.get("SECRET_KEY");
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("SECRET_KEY is not set in the environment variables");
        }
        return secretKey;
    }

    private static long getTokenExpiration() {
        return 3600000L;  // 1 час в миллисекундах
    }
}

