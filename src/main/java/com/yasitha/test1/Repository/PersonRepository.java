package com.yasitha.test1.Repository;

import com.yasitha.test1.Model.Person;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {
    @Query("SELECT p FROM Person p WHERE p.email = :email")
     Person findByEmail(@Param("email") String email);

    @Query("SELECT p FROM Person p WHERE p.username = :username")
    Person findByUsername(@Param("username") String username);



}
