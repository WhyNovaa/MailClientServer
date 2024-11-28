package tools;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Date;

public class JWT {

    public static String createJwt(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600)) // 1 час
                .signWith(SignatureAlgorithm.HS256, SecretKeyLoader.getSecretKey())
                .compact();
    }

    public static void validateJwt(String token) {
        Jwts.parser()
                .setSigningKey(SecretKeyLoader.getSecretKey())
                .parseClaimsJws(token);
        System.out.println("JWT is valid!");
    }
}


class SecretKeyLoader {
    private static final Dotenv dotenv = Dotenv.configure()
            .filename(".env")
            .load();

    public static String getSecretKey() {
        return dotenv.get("SECRET_KEY");
    }
    public static int getTokenExpiration() {
        return Integer.parseInt(dotenv.get("JWT_TIME"));
    }
}