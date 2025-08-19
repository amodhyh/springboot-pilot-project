package com.yasitha.test1.Service;

import com.yasitha.test1.ExceptionHandling.UserNotFoundException;
import com.yasitha.test1.Model.Person;
import com.yasitha.test1.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    /*  UserDetailsService to load user-specific data from the database.
     It retrieves user information from the PersonRepository based on the email.
     If the user is not found, it throws a UserNotFoundException.
     The roles of the user are converted to GrantedAuthority objects for Spring Security.
        Person -> UserDetails object with authorities
    */
    private final PersonRepository personRepository;

    @Autowired
    public CustomUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        Person person = personRepository.findByEmail(email);
        if (person == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        // Convert  roles to granted authorities
        // singleton returns an immutable set which only contains a single element

//        Collection<? extends GrantedAuthority> authorities= Collections.singleton(
//                new SimpleGrantedAuthority("Role_"+person.getRoles()));

        //As there will be multiple roles for a person, we need
        // to convert the roles to a collection of GrantedAuthority
        Set<GrantedAuthority> authorities=new HashSet<>();

        person.getRoles().forEach(role->{
            //converts each role to a GrantedAuthority
           authorities.add(new SimpleGrantedAuthority(role.getName()));
           role.getPermissions().forEach(permission->{
                //converts each permission to a GrantedAuthority
                authorities.add(new SimpleGrantedAuthority(permission.getPermissionName()));
           });

        });
        // The authorities set now contains both roles and permissions as GrantedAuthority objects
        //create and return the UserDetails object with the person's details and authorities

        return new User(
          person.getEmail(),
          person.getPassword(),
          authorities
        );


    }
}
