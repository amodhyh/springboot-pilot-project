package com.yasitha.test1.Utility;

import com.yasitha.test1.Model.Person;
import com.yasitha.test1.Model.Role;
import com.yasitha.test1.Service.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.apache.catalina.authenticator.jaspic.SimpleAuthConfigProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.sql.Date;
import java.util.*;

@Component
public class JWTUtility {
    //generate secret key from a external source

    //to generate in run time
    //final private Key secretKey = io.jsonwebtoken.security.Keys.secretKeyFor(SignatureAlgorithm.HS256);

    //expiration time
    // 1 hour in milliseconds
    @Getter
    private Date expirationTime;
    //final private Date expirationTime = new Date(System.currentTimeMillis()+ 3600000);
    @Getter
    private final SecretKey secretKey;


    public JWTUtility(@Value("${security.jwt.secret-key}") String secretKeyString,@Value("${security.jwt.expiration-time}") String expirationTimeString) {
        // Decode the Base64-encoded secret key string
        // Keys.hmacShaKeyFor() correctly handles the byte array to create the SecretKey
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyString));
        long expirationMillis = Long.parseLong(expirationTimeString);
        this.expirationTime = new Date(expirationMillis);
    }


    //Generate JWT token
    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {

        return io.jsonwebtoken.Jwts.builder()
                .setSubject(username)
                .claim("auth",authorities)
                .setExpiration(new java.util.Date(System.currentTimeMillis() + expirationTime.getTime()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSecretKey())
                .compact();
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
    // Extract roles from JWT token
//    public String extractRoles(String token) {
//        Map<String, Object> claims = Jwts.parserBuilder()
//                .setSigningKey(getSecretKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        Set<Role> roles = new HashSet<>();
//        if (claims.containsKey("roles")) {
//            Set<String> roleNames = (Set<String>) claims.get("roles");
//            for (String roleName : roleNames) {
//                roles.add(new Role(roleName));
//            }
//        }
//        return roles;
//    }



    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token is invalid or expired
        }
    }



    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        java.util.Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new java.util.Date());
    }
}



