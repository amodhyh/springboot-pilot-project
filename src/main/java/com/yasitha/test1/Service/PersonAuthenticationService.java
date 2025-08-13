package com.yasitha.test1.Service;

import com.yasitha.test1.DTO.PersonLogingRequest;
import com.yasitha.test1.ExceptionHandling.EmailNotFoundException;
import com.yasitha.test1.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class PersonAuthenticationService {

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public PersonAuthenticationService( CustomUserDetailsService customUserDetailsService, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
    }

    // Authenticate user with email and password
    public ResponseEntity<String> authenticateUser(PersonLogingRequest personLogingRequest) {
        // Check if the user exists by email
        if (customUserDetailsService.loadUserByUsername(personLogingRequest.getEmail()) == null) {
            throw new EmailNotFoundException("Email not found: " + personLogingRequest.getEmail());
        }
        //the email exists and then checks for the password using the AuthenticationManager.
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    personLogingRequest.getEmail(),
                    personLogingRequest.getPassword());

            // Authenticate the user give token for an authentication request
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            if (authentication.isAuthenticated()) {
                return ResponseEntity.ok("Login successfully");

            }
        } catch (AuthenticationException e) { //Catch for the wrong password
            return  ResponseEntity.status(401).body("Wrong Password");
        }
        return ResponseEntity.status(500).body("Unexpected error occurred");
    }
}
