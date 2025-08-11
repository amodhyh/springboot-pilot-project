package com.yasitha.test1.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Entity
@Table(name="PERSON")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
    @SequenceGenerator(name = "person_seq", sequenceName="PERSON_SEQ", allocationSize = 1)
    @Column(name = "PERSON_ID")
    @Getter @Setter
    private Long personId;

    @Builder.Default
    @Getter @Setter
    //many to many relationship with roles 1user-> many roles many users -> 1 role
    // EAGER fetch type is used to load roles immediately with the person
    // CascadeType.PERSIST and CascadeType.MERGE are used to propagate changes to roles when persisting or merging a person
    @ManyToMany(fetch = FetchType.EAGER,cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "Person_Role",
            joinColumns = @JoinColumn(name = "PERSON_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roles=new HashSet<>();

    @Column(name = "FIRST_NAME")
    @Getter @Setter
    private String firstName;

    @Column(name = "LAST_NAME")
    @Getter @Setter
    private String lastName;

    @Column(name = "EMAIL", unique = true)
    @Getter @Setter
    private String email;

    @Column(name = "DATE_OF_BIRTH")
    @Getter @Setter
    private LocalDate dob;

    @Column(name = "CREATED_AT",updatable = false)
    @Getter @Setter
    private LocalDate createdAt;

    @Column(name= "USER_NAME")
     @Setter
    private  String username;

    @Column(name = "PASSWORD",columnDefinition = "CLOB",length = 255, nullable = false)
     @Setter
    private String password;

    @Override
    //generate authorities based on roles and permissions
    //? anytype (wildcard)
    //extends GrantedAuthority means that the collection can contain any object that implements GrantedAuthority
    public Collection<? extends GrantedAuthority> getAuthorities() {
//return a stream of authorities combining roles and permissions(granted authorities objects)
            return Stream.concat(
                    roles.stream()
        // map each role to a SimpleGrantedAuthority
                            .map(role -> new SimpleGrantedAuthority( role.getName())),
                    roles.stream()
                            .flatMap(role -> role.getPermissions().stream()
                                    .map(perm -> new SimpleGrantedAuthority(perm.getPermissionName())))
            ).collect(Collectors.toSet());
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Or return a value from your entity
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Or return a value from your entity
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Or return a value from your entity
    }

    @Override
    public boolean isEnabled() {
        return true; // Or return a value from your entity
    }


}



