package com.yasitha.test1.Utility;

public class JWTUtility {
    // This class will contain methods for generating and validating JWT tokens.
    public String generateToken(String username) {
        // Logic to generate JWT token
        return "generatedToken"; // Placeholder
    }

    public boolean validateToken(String token, String username) {
        // Logic to validate JWT token
        return true;
    }

    public String  extractDetails(String token) {
        // Logic to extract username from JWT token
        return "extractedUsername"; // Placeholder
    }
}
