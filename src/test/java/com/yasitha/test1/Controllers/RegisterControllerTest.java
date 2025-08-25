package com.yasitha.test1.Controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yasitha.test1.DTO.PersonRegisterRequest;
import com.yasitha.test1.DTO.PersonRegisterResponse;
import com.yasitha.test1.Repository.PersonRepository;
import com.yasitha.test1.Security.JwtAuthenticationFilter;
import com.yasitha.test1.Service.CustomUserDetailsService;
import com.yasitha.test1.Service.PersonRegistrationService;
import com.yasitha.test1.Utility.JWTUtility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.nio.channels.ReadPendingException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegisterController.class,excludeAutoConfiguration =  { SecurityAutoConfiguration.class, JwtAuthenticationFilter.class})
@AutoConfigureJsonTesters
@Import(RegisterControllerTest.RegisterControllerTestContext.class)
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRegistrationService personRegistrationService;

    @Autowired
    private  PersonRepository personRepository;

    @Autowired
    private  DelegatingPasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @TestConfiguration
    static class RegisterControllerTestContext{
        @Bean
        public PersonRegistrationService personRegistrationService(){
            return mock(PersonRegistrationService.class);
        }
        @Bean
        public PersonRepository personRepository(){
            return mock(PersonRepository.class);
        };
        @Bean
        public DelegatingPasswordEncoder passwordEncoder(){
            return mock(DelegatingPasswordEncoder.class);
        };

        @Bean
        public JWTUtility jwtUtility(){
            return mock(JWTUtility.class);
        }
        @Bean
        public CustomUserDetailsService customUserDetailsService(){
            return mock(CustomUserDetailsService.class);
        }
        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            // Register the JavaTimeModule to handle Java 8 Date/Time types
            mapper.registerModule(new JavaTimeModule());
            return mapper;
        }
        }

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testRegister() throws Exception {
        PersonRegisterRequest request = new PersonRegisterRequest(

                "Doe@email.com",
                "12345678",
                "johndoe",
                "John",
                "Doe",
                java.time.LocalDate.of(2000, 1, 1));

        PersonRegisterResponse response = new PersonRegisterResponse("User registered successfully");
        when(personRegistrationService.registerUser(any(PersonRegisterRequest.class)))
                .thenReturn(ResponseEntity.status(200).body(response));

        mockMvc.perform(post("/reg")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }
    @Test
    public void testRegister_InvalidInput_ReturnsBadRequest() throws Exception {
        PersonRegisterRequest request = new PersonRegisterRequest(
                "invalid-email", // Invalid email format
                "short",        // Password too short
                "",             // Empty username
                "John",
                "Doe",
                java.time.LocalDate.of(2026, 1, 1) // Future date of birth
        );
         // Perform the POST request and expect a 400 Bad Request status
         mockMvc.perform(post("/reg")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                 .andExpect(status().isBadRequest())
                 .andExpect(jsonPath("$.messages[0]").value("Invalid email format"))
                 .andExpect(jsonPath("$.messages[1]").value("Username is required"))
                 .andExpect(jsonPath("$.messages[2]").value("Date of birth must be in the past"))
                 .andExpect(jsonPath("$.messages[3]").value("Password Should be minimum 8 characters and maximum 15 characters"));

    }
    @Test
    public void testRegister_AgeBelow_16() throws Exception {
        PersonRegisterRequest request = new PersonRegisterRequest(
                "testemail@gmail.com",
                "12345678",
                "testuser",
                "TestName",
                "TestLastName",
                java.time.LocalDate.of(2011, 1, 1) // User is 14 years old

        );
        PersonRegisterResponse response = new PersonRegisterResponse("User must be at least 16 years old to register.");

        when(personRegistrationService.registerUser(any(PersonRegisterRequest.class))).thenReturn(
                ResponseEntity.status(400).body(response)
        );

        mockMvc.perform(post("/reg")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User must be at least 16 years old to register."));

    }
}
