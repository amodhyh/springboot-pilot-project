package com.yasitha.test1.Service;


import com.yasitha.test1.DTO.PersonLoginResponse;
import com.yasitha.test1.DTO.PersonLogingRequest;
import com.yasitha.test1.ExceptionHandling.InvalidCredentials;
import com.yasitha.test1.ExceptionHandling.UserNotFoundException;
import com.yasitha.test1.Utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;



@Service
public class PersonAuthenticationService {
    private final JWTUtility jwtUtility;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
   // private final ObjectValidationService objectValidationService;

    @Autowired
    public PersonAuthenticationService(JWTUtility jwtUtility, CustomUserDetailsService customUserDetailsService, AuthenticationManager authenticationManager) {
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
       // this.objectValidationService = objectValidationService;
    }

    // Authenticate user with email and password
    public ResponseEntity<PersonLoginResponse> authenticateUser(PersonLogingRequest personLogingRequest) {
        // Input Valdation
        //objectValidationService.validate(personLogingRequest);

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
            throw new InvalidCredentials("Invalid email or password");

        }
        catch (UserNotFoundException e) { //Catch for the wrong email
            throw new UserNotFoundException("User not Found");        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PersonLoginResponse(" Unexpected error occurred during authentication"));
    }
}
