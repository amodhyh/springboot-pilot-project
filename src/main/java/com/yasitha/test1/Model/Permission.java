package com.yasitha.test1.Model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name="PERMISSIONS")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PERMISSION_ID")
    private Long id;

    @Column(name="PERMISSION_NAME", unique = true, nullable = false)
    @Getter @Setter
    private String permissionName;

    @Getter
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;


}
