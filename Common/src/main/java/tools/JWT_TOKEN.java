package tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Date;

public class JWT_TOKEN {
    public static String createJwt(String subject) {
            Algorithm algorithm = Algorithm.HMAC256(Env.getSecretKey().getBytes());

            return JWT.create()
                    .withSubject(subject)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + Env.getTokenExpiration())) // Время истечения (1 час)
                    .sign(algorithm);
    }

    public static boolean validateJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(Env.getSecretKey());

            JWTVerifier verifier = JWT.require(algorithm).build();

            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }
}

