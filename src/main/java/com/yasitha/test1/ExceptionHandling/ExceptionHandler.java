package com.yasitha.test1.ExceptionHandling;

import com.yasitha.test1.DTO.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    //Handle Exceptions Globally defining methods

    //User is not on the database
    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleUserNotFoundException(UserNotFoundException ex) {
        // Return a 404 Not Found response for a specific custom exception
        ErrorDTO userNotFoundError = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(userNotFoundError, HttpStatus.NOT_FOUND);

    }
    //email not found in the database
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<ErrorDTO> handleEmailNotFoundException(EmailNotFoundException ex) {
        // Return a 404 Not Found response for email not found
        ErrorDTO emailNotFoundError = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(emailNotFoundError, HttpStatus.NOT_FOUND);
    }

    //If the email already exists in the database
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<ErrorDTO> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        // Return a 409 Conflict response for email already exists
        ErrorDTO emailError = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(emailError, HttpStatus.CONFLICT);
    }
    //User age it should be more than 16
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<ErrorDTO> handleUserAgeException(UserAgeException ex) {
        // Return a 400 Bad Request response for user age validation failure
        ErrorDTO ageError = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(ageError, HttpStatus.BAD_REQUEST);
    }

}
