package org.example.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;

@Component
public class TokenService {

    private final String SECRET_KEY = "mY@Secret!KEy_";
    private final long ACCESSTOKEN_EXPIRATION_TIME = 1000 * 60 * 5; // 3 минуты
    private final long REFRESHTOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7 дней
private  final  SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    // Генерация access токена
    public String generateAccessToken(String login, SecretKey key) {
        return createToken( login,key);
    }

    // Генерация refresh токена
    public String generateRefreshToken(HashMap<String, String> refreshClaims, String login) {
        return createRefreshToken(refreshClaims, login);
    }

    // Создание access токена
    private String createToken(String subject,SecretKey key) {
      return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESSTOKEN_EXPIRATION_TIME))
                .signWith(key)
                .compact();

    }
public HashMap<String,String> getTokenClaims(String token){
        Claims claims=Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        HashMap<String,String> map=new HashMap<>();
  map.put("sub",claims.getSubject());
    map.put("iat",claims.getIssuedAt().toString());
    map.put("exp",claims.getExpiration().toString());
    return map;
}

    private String createRefreshToken(HashMap<String, String> claims, String subject) {

        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESHTOKEN_EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            // if token expire return true
            return isTokenExpired(claims.getExpiration());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Проверка на истечение токена
    private boolean isTokenExpired(Date expiration) {
        // мы передаем срок когда токен итекает например 25 октября
        // 25.10 раньше сегодня (25.10 > 24.10) false  значит что токен не истек он позже чем сегодня
        // 25.10 раньше 28.10 25.10 25.10 < 28 true токен истек он раньше чем сегодня
        return expiration.before(new Date());
    }

    // Получение имени пользователя из токена
    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}
