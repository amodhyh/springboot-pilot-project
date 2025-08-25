package com.yasitha.test1.Service;

import com.yasitha.test1.DTO.PersonRegisterRequest;
import com.yasitha.test1.DTO.PersonRegisterResponse;
import com.yasitha.test1.ExceptionHandling.EmailAlreadyExistsException;
import com.yasitha.test1.ExceptionHandling.UserAgeException;
import com.yasitha.test1.Model.Person;
import com.yasitha.test1.Model.Role;
import com.yasitha.test1.Repository.PersonRepository;
import com.yasitha.test1.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

@Service
public class PersonRegistrationService {

    private final PersonRepository personRepository;
    private final DelegatingPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public PersonRegistrationService( RoleRepository roleRepository, PersonRepository personRepository, DelegatingPasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }
    private boolean isAnyFieldNull(PersonRegisterRequest request) {
        return request.getEmail() == null ||
                request.getPassword() == null ||
                request.getDob() == null ||
                request.getUsername() == null ||
                request.getFirstName() == null ||
                request.getLastName() == null;
    }

    @Transactional
    public ResponseEntity<PersonRegisterResponse> registerUser(PersonRegisterRequest personRegisterRequest)  {

         if(personRepository.findByEmail(personRegisterRequest.getEmail())!= null) {
            throw new EmailAlreadyExistsException(personRegisterRequest.getEmail()+" Email already exists!");
        }
        else if (Period.between(personRegisterRequest.getDob(), LocalDate.now()).getYears()<16) {
            throw new UserAgeException("User must be at least 16 years old to register.");

        }

        Role defRole= roleRepository.findByName("USER");
        Set<Role> defRoleSet = new HashSet<>();
        defRoleSet.add(defRole);
        String encryptedPassword = passwordEncoder.encode(personRegisterRequest.getPassword());
        Person tem=Person.builder()
                .firstName(personRegisterRequest.getFirstName())
                .lastName(personRegisterRequest.getLastName())
                .email(personRegisterRequest.getEmail())
                .username(personRegisterRequest.getUsername())
                .password(encryptedPassword)
                .dob(personRegisterRequest.getDob())
                .createdAt(LocalDate.now())
                .roles(defRoleSet)
                .build();

        personRepository.saveAndFlush(tem);
        return ResponseEntity.status(200).body(new PersonRegisterResponse(personRegisterRequest.getUsername()+" User registered successfully"));


    }


}
