package com.yasitha.test1.Security;

import com.yasitha.test1.Service.CustomUserDetailsService;
import com.yasitha.test1.Utility.JWTUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Filter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // This class will handle JWT authentication for incoming requests.
    // It will extend OncePerRequestFilter to ensure that the filter is applied once per request.

    private final JWTUtility jwtUtility;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JWTUtility jwtUtility, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtility = jwtUtility;
        this.customUserDetailsService = customUserDetailsService;
    }
    // Override the doFilterInternal method to implement JWT authentication logic.
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)throws ServletException, IOException {
        String authorizationHeader = req.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            try {
                // Validate the JWT token and extract user details
                String username = jwtUtility.extractDetails(jwtToken);
                if (username != null) {
                    // Load user details from the database
                    var userDetails = customUserDetailsService.loadUserByUsername(username);
                    // Set the authentication in the security context
                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Handle exceptions related to JWT validation
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        }
    }

}
