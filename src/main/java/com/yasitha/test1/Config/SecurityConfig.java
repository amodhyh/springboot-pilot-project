package com.yasitha.test1.Config;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.net.PasswordAuthentication;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public DefaultSecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/reg","/login").permitAll()
                        .requestMatchers("/home").authenticated()
                        .requestMatchers("/settings").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable());
        return http.build();
    }



    @Bean
    public DelegatingPasswordEncoder passwordEncoder() throws Exception {
    Argon2PasswordEncoder argon2PasswordEncoder=new Argon2PasswordEncoder(20, 65536, 1, 32, 16);
        return new DelegatingPasswordEncoder("argon2", Map.of("argon2", argon2PasswordEncoder));
    }


}
