package com.yasitha.test1.Utility;

import com.yasitha.test1.Model.Role;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.sql.Date;

public class JWTUtility {
    final private Key secretKey = io.jsonwebtoken.security.Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // 1 hour in milliseconds
    final private Date expirationTime = new Date(System.currentTimeMillis()+ 3600000);

    //Generate JWT token
    public String generateToken(String email,Role role) {
        return io.jsonwebtoken.Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setExpiration(expirationTime)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(secretKey)
                .compact();
    }

    // Validate JWT token
    public boolean validateToken(String token, String username) {
        try {
            String extractedUsername = extractDetails(token);
            return extractedUsername.equals(username) && !isTokenExpired(token);

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Extract username from JWT token
    public String extractDetails(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        java.util.Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new java.util.Date());
    }
}


}
