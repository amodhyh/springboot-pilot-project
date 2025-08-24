package com.yasitha.test1.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yasitha.test1.DTO.ErrorDTO;
import com.yasitha.test1.DTO.PersonLoginResponse;
import com.yasitha.test1.DTO.PersonLogingRequest;
import com.yasitha.test1.Security.JwtAuthenticationFilter;
import com.yasitha.test1.Service.CustomUserDetailsService;
import com.yasitha.test1.Service.PersonAuthenticationService;
import com.yasitha.test1.Utility.JWTUtility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoginController.class,
        excludeAutoConfiguration = {JwtAuthenticationFilter.class, SecurityAutoConfiguration.class}
)
public class LoginControllerTest {
    //What @Mock does?
    // Create a fake Standalone Object - mock instance of PersonAuthenticationService
    //When you need a dependency that your "object under test" (the real object you're trying to test) relies on.
    // You don't need to manually create an instance of this mock yourself.
    //it can be defined such as this mock to define its behavior using when().thenReturn() and verify its interactions using verify().
    //even though personAuthSer uses CustomUserDetailsService and JWTUtility, we don't need to mock them here.
    //why? because we are not testing personAuthSer here, we are testing LoginController.

    //@InjectMock -> Create a real instance of this class, and automatically inject all the mocks I've created into it
    //Creates a real instance of the annotated class and automatically injects any fields that are @Mocks or @Spys into it.
    //When to use it: On the class you are actually testing. This is your "System Under Test" (SUT).
    @Autowired
    private MockMvc mockMvc;

    //Create custom beans to the Spring test context
    @TestConfiguration
    static class LoginControllerTestContext {
        @Bean
        public PersonAuthenticationService personAuthenticationService() {
            return mock(PersonAuthenticationService.class);
        }
        @Bean
        public CustomUserDetailsService customUserDetailsService() {
            return mock(CustomUserDetailsService.class);
        }
        @Bean
        public JWTUtility jwtUtility() {
            return mock(JWTUtility.class);
        }
        @Bean
        public AuthenticationManager authenticationManager() {
            return mock(AuthenticationManager.class);
        }

    }
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PersonAuthenticationService personAuthenticationService;

    @Test
    public void testAuthenticate_ValidCredentials_ReturnsTokens() throws Exception {
        // Arrange
        // Note: We're not using the DTO object here to avoid any potential
        // validation issues with the test environment.
        // Instead, we use a clean JSON string.
        //String requestBodyJson = "{\"email\":\"test@email.com\",\"password\":\"password123\"}";
        PersonLogingRequest personLogingRequest = new PersonLogingRequest("test@email.com", "password123");
        // The response is still mocked correctly as before
        PersonLoginResponse loginResponse = new PersonLoginResponse("mocked-jwt-token", "Login successful");

        when(personAuthenticationService.authenticateUser(any(PersonLogingRequest.class)))
                .thenReturn(ResponseEntity.ok(loginResponse));
        // Act & Assert
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(personLogingRequest))) // Use the hardcoded string
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    public void testAuthenticate_InvalidCredentials_ReturnsError() throws Exception {
        // Arrange
        PersonLogingRequest loginRequest = new PersonLogingRequest("wrong@email.com", "wrongpassword");
//        String requestBodyJson = "{\"email\":\"test@email.com\",\"password\":\"password123\"}";
        PersonLoginResponse loginResponse = new PersonLoginResponse("Invalid email or password");

        when(personAuthenticationService.authenticateUser(any(PersonLogingRequest.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse));
        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                    .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    public void testAuthenticate_MissingEmail_ReturnsBadRequest() throws Exception {
        // Arrange
        PersonLogingRequest loginRequest = new PersonLogingRequest(null, "password123");
        //MultiErrorDTO multiErrorDTO = new MultiErrorDTO( Set.of("Email is required",""));
        PersonLoginResponse loginResponse = new PersonLoginResponse(new ErrorDTO(Set.of("Email is required","")));

        when(personAuthenticationService.authenticateUser(any(PersonLogingRequest.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body((loginResponse)));
        // Act & Assert
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("Email is required"));
    }


    @Test
    public void testAuthenticate_MissingPassword_ReturnsBadRequest() throws Exception {
        // Arrange
        PersonLogingRequest loginRequest = new PersonLogingRequest("test@email.com", null);
        PersonLoginResponse loginResponse = new PersonLoginResponse("Password is required");

        when(personAuthenticationService.authenticateUser(any(PersonLogingRequest.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse));
        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("Password is required"));

    }

    @Test
    public void testAuthenticate_InvalidEmailFormat_ReturnsBadRequest() throws Exception {
        // Arrange
        PersonLogingRequest loginRequest = new PersonLogingRequest("testemail.com", "123");
        PersonLoginResponse loginResponse = new PersonLoginResponse("Invalid Email Format");

        when(personAuthenticationService.authenticateUser(any(PersonLogingRequest.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse));
        // Act & Assert
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Password is required"));

    }
    @Test
    public void testAuthenticate_NoCredentials_ReturnsBadRequest() throws Exception {
        // Arrange
        PersonLogingRequest loginRequest = new PersonLogingRequest("", "");
        PersonLoginResponse loginResponse = new PersonLoginResponse("Invalid Email Format");

        when(personAuthenticationService.authenticateUser(any(PersonLogingRequest.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse));
        // Act & Assert
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email is required,Password is required"));

    }



}