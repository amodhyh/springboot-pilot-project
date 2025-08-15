package com.yasitha.test1.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.yasitha.test1.DTO.PersonLogingRequest;
import com.yasitha.test1.ExceptionHandling.EmailNotFoundException;
import com.yasitha.test1.Repository.PersonRepository;
import com.yasitha.test1.Utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class PersonAuthenticationService {
    private final JWTUtility jwtUtility;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public PersonAuthenticationService(JWTUtility jwtUtility, CustomUserDetailsService customUserDetailsService, AuthenticationManager authenticationManager) {
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
    }

    // Authenticate user with email and password
    public ResponseEntity<Map<String,String>> authenticateUser(PersonLogingRequest personLogingRequest) {
        // Check if the user exists by email\
        HashMap<String, String> message = new HashMap<>();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(personLogingRequest.getEmail());
        if (userDetails == null) {
            throw new EmailNotFoundException("Email not found: " + personLogingRequest.getEmail());
        }
        //the email exists and then checks for the password using the AuthenticationManager.
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    personLogingRequest.getEmail(),
                    personLogingRequest.getPassword());

            // Authenticate the user give token for an authentication request
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            ObjectMapper objectMapper = new ObjectMapper();
            if (authentication.isAuthenticated()) {

                String token=jwtUtility.generateToken(userDetails.getUsername(), userDetails.getAuthorities());

                String jsonResponse = objectMapper.writeValueAsString(token);

                message.put("token", jsonResponse);
                message.put("message", "User authenticated successfully");
                return ResponseEntity.ok(message);

            }
        } catch (AuthenticationException e) { //Catch for the wrong password
            message.put("message", "Invalid username or password");
            return  ResponseEntity.status(401).body(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        message.put("message", "Unexpected Error Occured");
        return ResponseEntity.status(500).body(message);
    }
}
