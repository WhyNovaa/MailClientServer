package tools;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.SignatureException;

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

    public static boolean validateJwt(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecretKeyLoader.getSecretKey())
                    .parseClaimsJws(token); // Проверяет токен
            return true;
        } catch (Exception e) {
            return false;
        }
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