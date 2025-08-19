package com.yasitha.test1.Service;

import com.yasitha.test1.DTO.PersonRegReq;
import com.yasitha.test1.ExceptionHandling.EmailAlreadyExistsException;
import com.yasitha.test1.ExceptionHandling.UserAgeException;
import com.yasitha.test1.Model.Person;
import com.yasitha.test1.Model.Role;
import com.yasitha.test1.Repository.PermissionRepo;
import com.yasitha.test1.Repository.PersonRepository;
import com.yasitha.test1.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public String registerUser(PersonRegReq personRegReq)  {

        if(personRepository.findByEmail(personRegReq.getEmail())!= null) {
            throw new EmailAlreadyExistsException(personRegReq.getEmail()+" Email already exists!");
        }
        else if (Period.between(personRegReq.getDob(), LocalDate.now()).getYears()<16) {
            throw new UserAgeException("User must be at least 16 years old to register.");

        }

        Role defRole= roleRepository.findByName("ADMIN");
        Set<Role> defRoleSet = new HashSet<>();
        defRoleSet.add(defRole);
        String encryptedPassword = passwordEncoder.encode(personRegReq.getPassword());
        Person tem=Person.builder()
                .firstName(personRegReq.getFirstName())
                .lastName(personRegReq.getLastName())
                .email(personRegReq.getEmail())
                .username(personRegReq.getUsername())
                .password(encryptedPassword)
                .dob(personRegReq.getDob())
                .createdAt(LocalDate.now())
                .roles(defRoleSet)
                .build();

        personRepository.saveAndFlush(tem);
        return "User registered successfully with username: " + personRegReq.getUsername();


    }


}
