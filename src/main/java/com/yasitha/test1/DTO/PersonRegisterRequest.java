package com.yasitha.test1.DTO;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class PersonRegisterRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=8,max=15,message ="Password Should be minimum 8 characters and maximum 15 characters" )
    private String password;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "first name is required")
    private String firstName;

    @NotBlank(message = "last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required") //NotBlank can be only used for the string fields
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;
}
