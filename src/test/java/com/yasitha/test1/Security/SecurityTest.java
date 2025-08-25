package com.yasitha.test1.Security;

import com.yasitha.test1.Service.CustomUserDetailsService;
import com.yasitha.test1.Utility.JWTUtility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTUtility jwtUtility;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CustomUserDetailsService customUserDetailsService() {
            //create a mock userdetails object when the mock service is called
            CustomUserDetailsService mockService = mock(CustomUserDetailsService.class);
            when(mockService.loadUserByUsername("testname"))
                    .thenReturn(org.springframework.security.core.userdetails.User
                            .withUsername("testname")
                            .password("password")
                            .roles("USER")
                            .build());
            return mockService;
        }
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void whenTokenIsValidAccessIsGranted() throws Exception {

        String username ="testname"; // "testemail@email.com"
        Collection<? extends GrantedAuthority> authorities = Set.of(()->"USER");
        String token = jwtUtility.generateToken(username, authorities);

        mockMvc.perform(get("/home")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

    }

    @Test
   //mock user would automatically add a user to the security context therefore it would bypass the jwtsec filter
    public void whenTokenIsMissingAccessIsDenied() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = {"USER"})
    public void privilageTest() throws Exception {
    //user is authenticated and in the sec context but has no permission to access this page ->forbidden
        mockMvc.perform(get("/settings"))
                .andExpect(status().isForbidden());
    }
    @Test
    public void withoutCredentials_ProtectedEndpoint_DeniesAccess()  throws Exception {
        //user is not authenticated and not in the sec context ->forbidden
        mockMvc.perform(get("/settings"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void withInvalidToken_DenyAccess()  throws Exception {
        String token="invalidtoken";
        //user is not authenticated and not in the sec context ->Unauthorised
        mockMvc.perform(get("/home")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }
}



