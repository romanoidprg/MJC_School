package com.epam.esm.utils;

import com.epam.esm.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private final static Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final String jwtIssuer = "Roman";

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 10000))
                .signWith(KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validate(String token) {
        try {
            String s = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
