package com.yasitha.test1.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.yasitha.test1.DTO.PersonLoginResponse;
import com.yasitha.test1.DTO.PersonLogingRequest;
import com.yasitha.test1.ExceptionHandling.EmailNotFoundException;
import com.yasitha.test1.ExceptionHandling.UserNotFoundException;
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
    public ResponseEntity<PersonLoginResponse> authenticateUser(PersonLogingRequest personLogingRequest) {
        // Check if the user exists by email\


        //the email exists and then checks for the password using the AuthenticationManager.
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(personLogingRequest.getEmail());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    personLogingRequest.getEmail(),
                    personLogingRequest.getPassword());

            // Authenticate the user give token for an authentication request
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            if (authentication.isAuthenticated()) {

                String token=jwtUtility.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
                return ResponseEntity.ok(new PersonLoginResponse(token,"login Succesfull!"));

            }
        } catch (AuthenticationException e) { //Catch for the wrong password
            return  ResponseEntity.status(401).body(new PersonLoginResponse("Invalid email or password"));

        }
        catch (UserNotFoundException e) { //Catch for the wrong email
            return  ResponseEntity.status(404).body(new PersonLoginResponse("Email not found"));
        }

        return ResponseEntity.status(500).body(new PersonLoginResponse(" Unexpected error occurred during authentication"));
    }
}
