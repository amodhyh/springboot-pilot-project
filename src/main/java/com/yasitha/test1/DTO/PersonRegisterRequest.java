package com.yasitha.test1.DTO;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonRegisterRequest {
    @NotNull(message = "Email is required")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min=8,max=15,message ="Password Should be minimum 8 characters and maximum 15 characters" )
    private String password;

    @NotNull(message = "Username is required")
    @NotBlank(message = "Username is required")
    private String username;

    @NotNull(message = "First name is required")
    @NotBlank(message = "first name is required")
    private String firstName;

    @NotNull(message = "Last name is required")
    @NotBlank(message = "last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required") //NotBlank can be only used for the string fields
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;
}
