package org.demointernetshop0505.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenProvider {

    private String jwtSecret = "98a76rg98agkjashgkjhsagfsaf8g7tofsa8ga";

    private long jwtLifeTime = 600000; // 10 минут


    // создание нового токена JWT
    public String createToken(String username) {

        Date now = new Date();

        Date expiryDate = new Date(now.getTime() + jwtLifeTime);

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    // проверка (валидация) полученного от пользователя токена
    public boolean validateToken(String token) {

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            throw new InvalidJwtException("Invalid JWT token: " + e.getMessage());
        }

        return true;
        /*
        SignatureException - Invalid JWT Signature
        MalformedJwtException - Invalid JWT token
        ExpiredJwtException - Expired Jwt token
        UnsupportedJwtException - Unsupported Jwt token
        IllegalArgumentException - JWT claims is empty
         */

    }

    //вытаскивать из токена (из payload) имя пользователя (username)
    public String getUsernameFromJwt(String token) {

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        Claims claimsPayload = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claimsPayload.getSubject();

    }

}
