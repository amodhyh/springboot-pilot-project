package com.yasitha.test1.Repository;

import com.yasitha.test1.Model.Person;
import com.yasitha.test1.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public  interface RoleRepository extends JpaRepository<Role,Long> {
   Role findByName(String rolename);
   Role findRoleById(Long id);
}
