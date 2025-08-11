package com.yasitha.test1.DTO;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@Getter
@Setter
public class PersonRegReq {
    private Long personId;
    private String email;
    private String password;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private LocalDate createdAt;
}
