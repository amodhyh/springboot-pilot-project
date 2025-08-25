package com.yasitha.test1.Controllers;

import com.yasitha.test1.DTO.PersonLoginResponse;
import com.yasitha.test1.DTO.PersonLogingRequest;
import com.yasitha.test1.Service.PersonAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    private final PersonAuthenticationService personAuthenticationService;

    @Autowired
    public LoginController(PersonAuthenticationService personAuthenticationService) {
        this.personAuthenticationService = personAuthenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<PersonLoginResponse> authentication(@Valid @RequestBody PersonLogingRequest request) {
        return personAuthenticationService.authenticateUser(request);

    };
    @GetMapping("/home")
    public ResponseEntity<Map<String, String>> home(HttpServletRequest request) {
        // This endpoint can be used to test if the user is authenticated.
        // It can return a simple message or user details.
        String username = request.getUserPrincipal().getName();
        return ResponseEntity.ok(Map.of("message", "Welcome " + username + "! You are authenticated."));
    }
    @GetMapping("/settings")
    public ResponseEntity<Map<String, String>> settings(HttpServletRequest request) {
        return ResponseEntity.ok(Map.of("message", "Settings page. Only accessible to ADMIN users."));
    }


}
