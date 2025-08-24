package com.yasitha.test1.ExceptionHandling;

import com.yasitha.test1.DTO.ErrorDTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Set;
import java.util.stream.Collectors;


@ControllerAdvice
public class ExceptionHandler {
    //Handle Exceptions Globally defining methods

    //User is not on the database
    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleUserNotFoundException(UserNotFoundException ex) {
        // Return a 404 Not Found response for a specific custom exception
        ErrorDTO userNotFoundError = new ErrorDTO(Set.of(ex.getMessage()));
        return ResponseEntity.status(404).body(userNotFoundError);

    }
    //email not found in the database
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<ErrorDTO> handleEmailNotFoundException(EmailNotFoundException ex) {
        // Return a 404 Not Found response for email not found
        ErrorDTO emailNotFoundError = new ErrorDTO(Set.of(ex.getMessage()));
        return  ResponseEntity.status(404).body(emailNotFoundError);
    }

    //If the email already exists in the database
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<ErrorDTO> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        // Return a 409 Conflict response for email already exists
        ErrorDTO emailError = new ErrorDTO(Set.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(emailError);
    }

    //User age it should be more than 16
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<ErrorDTO> handleUserAgeException(UserAgeException ex) {
        // Return a 400 Bad Request response for user age validation failure
        ErrorDTO ageError = new ErrorDTO(Set.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ageError);
    }
    //DTO Validation
//    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidArguments.class)
//    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(InvalidArguments ex) {
//
//        ErrorDTO methodArgumentNotValidError = new ErrorDTO(ex.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(methodArgumentNotValidError);
//    }

    //Invalid credentials exception
    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidCredentials.class)
    public ResponseEntity<ErrorDTO> handleInvalidCredentialsException(InvalidCredentials ex) {
        // Return a 401 Unauthorized response for invalid credentials
        ErrorDTO invalidCredentialsError = new ErrorDTO(Set.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(invalidCredentialsError);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Set<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toSet());
        ErrorDTO methodArgumentNotValidError = new ErrorDTO(errorMessages);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(methodArgumentNotValidError);
    }

}
