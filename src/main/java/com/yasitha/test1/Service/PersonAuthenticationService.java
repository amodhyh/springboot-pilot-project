package com.yasitha.test1.Service;

import com.yasitha.test1.DTO.PersonLogingRequest;
import com.yasitha.test1.ExceptionHandling.EmailNotFoundException;
import com.yasitha.test1.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonAuthenticationService {

    private final PersonRepository personRepository;
    private final DelegatingPasswordEncoder delegatingPasswordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public PersonAuthenticationService(PersonRepository personRepository, DelegatingPasswordEncoder delegatingPasswordEncoder, CustomUserDetailsService customUserDetailsService) {
        this.personRepository = personRepository;
        this.delegatingPasswordEncoder = delegatingPasswordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    public String authenticateUser(PersonLogingRequest personLogingRequest) {
        customUserDetailsService.loadUserByUsername(personLogingRequest.getEmail());
        return "s";
    }



}
