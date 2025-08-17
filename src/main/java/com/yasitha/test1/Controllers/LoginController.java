package com.yasitha.test1.Controllers;

import com.yasitha.test1.DTO.PersonLoginResponse;
import com.yasitha.test1.DTO.PersonLogingRequest;
import com.yasitha.test1.Service.PersonAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Map;

@RestController
public class LoginController {

    private final PersonAuthenticationService personAuthenticationService;

    @Autowired
    public LoginController(PersonAuthenticationService personAuthenticationService) {
        this.personAuthenticationService = personAuthenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<PersonLoginResponse> authentication(@RequestBody PersonLogingRequest request) {
        return personAuthenticationService.authenticateUser(request);

    };

}
