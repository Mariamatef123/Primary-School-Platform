// package com.first.first_app.Security;

// import java.util.Date;
// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.security.core.userdetails.UserDetails;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;

// public class JwtUtil {

//     private static final String SECRET = "secret123";

//   /**
//      * Generates a JWT token containing email (subject), user ID, and user role.
//      *
//      * @param email The user's email (used as the subject)
//      * @param userId The user's unique ID (added as a custom claim)
//      * @param userRole The user's role (e.g., ADMIN, TEACHER)
//      * @return The signed JWT string
//      */
//     public String generateToken(UserDetails email, Integer userId, String userRole) { // NOTE: Added userId parameter
//         Map<String, Object> claims = new HashMap<>();
        
//         // Add custom claims for role and ID (essential for authorization checks)
//         claims.put("role", userRole);
//         claims.put("userId", userId);
        
//         // Define expiration time (24 hours)
//         long expirationTimeMs = 24 * 60 * 60 * 1000; 

//         return Jwts.builder()
//                 .setClaims(claims)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMs))
//                 .compact();
//     }
// }
