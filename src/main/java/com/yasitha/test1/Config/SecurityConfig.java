package com.yasitha.test1.Config;

import com.yasitha.test1.Service.CustomUserDetailsService;
import com.yasitha.test1.Security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain  securityWebFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/reg","/login").permitAll()  //coarse-grained url based access control
                        .requestMatchers("/home").authenticated()
                        .requestMatchers("/settings").hasAuthority("ADMIN")
                        .anyRequest().authenticated())  //all ther endpoints should be authenticated

                        .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                        .formLogin(formLogin -> formLogin.disable())
                //Session manaagement is set to stateless to ensure that the application does not use sessions to store authentication information.
                        .sessionManagement(sessionManagement ->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .httpBasic(httpBasic -> httpBasic.disable());
        return http.build();
    }

    @Bean
    //receive a login request and delegate the actual work of authentication to one or more AuthenticationProvider
    public AuthenticationManager authenticationManagerBean(CustomUserDetailsService customUserDetailsService,  DelegatingPasswordEncoder passwordEncoder)  {
        //the specific provider that knows how to handle a username and password by checking a database.
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(customUserDetailsService);

        //ProviderManager is a concrete implementation of AuthenticationManager
        // that delegates authentication requests to a list of AuthenticationProvider instances.
        return new ProviderManager(authenticationProvider);




    }


    @Bean
    public DelegatingPasswordEncoder passwordEncoder() throws Exception {
    Argon2PasswordEncoder argon2PasswordEncoder=new Argon2PasswordEncoder(20, 65536, 1, 32, 16);
        return new DelegatingPasswordEncoder("argon2", Map.of("argon2", argon2PasswordEncoder));
    }


}
