package com.example.BookMySlot.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

    // Convert SecretKey to secretObject used for both signing and verifying
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // extracting email subject(email)
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Gets expirations time of token
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // Parse the token, verify using Secret Key, Extract and return all claims inside it
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // checks weather the token is already expired
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // creating token by passing email as a subject
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    // build JWT token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))  // 30 -> 30 minutes
                .signWith(getSigningKey())
                .compact();
    }

    // Simple wrapper method to return true if token is still valid
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
