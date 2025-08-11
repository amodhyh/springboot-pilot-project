package com.yasitha.test1.Model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ROLES")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "ROLE_ID")
    @Getter
    private Long id; // This is the Role's primary key


    @Getter @Setter
    @Column(name = "ROLE_NAME", unique = true, nullable = false)
    private String name;

    @Getter
    @ManyToMany(mappedBy ="roles" )
    private Set<Person> persons=new HashSet<>();

    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID"))
    Set<Permission> permissions=new HashSet<>();

    public Role(String name){
        this.name=name;
    }

    public Role() {

    }
}
