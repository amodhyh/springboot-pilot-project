package com.yasitha.test1.Security;

import com.yasitha.test1.ExceptionHandling.UserNotFoundException;
import com.yasitha.test1.Service.CustomUserDetailsService;
import com.yasitha.test1.Utility.JWTUtility;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = req.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            try {
                // Validate the JWT token and extract user details
                username = jwtUtility.extractUserDetails(jwtToken);
            } catch (JwtException e) {
                // If the JWT token is invalid, set the response status to 401 Unauthorized
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Load user details from the database
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // This validation step should also be here.
                if (jwtUtility.validateToken(jwtToken)) {
                    // Set the authentication in the security context
                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    /*
                    Security context temporary store  users identity and permissions for the
                    duration of a single http request
                    SecurityContext is an object held by the SecurityContextHolder
                     has 3 objects
                     Principle-UserDetails object
                     Credentials-null for the jwt
                     Authorities-User's granted authorities
                     */
                } else {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } catch (UserNotFoundException e) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        // Critical: Continue the filter chain.
        filterChain.doFilter(req, res);
    }
}
