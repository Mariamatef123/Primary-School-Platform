package com.first.first_app.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.first.first_app.Model.User;
import com.first.first_app.Service.UserService;



@RestController
public class UserController {
    
    @Autowired
    private UserService userService;
  

    

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User user) {
    User loggedInUser = userService.login(user);

    if (loggedInUser != null) {
   
        Map<String, Object> response = new HashMap<>();
        response.put("id", loggedInUser.getId()); 
        response.put("name", loggedInUser.getName());
        response.put("role", loggedInUser.getRole());
        return ResponseEntity.ok(response);
    } else {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Login failed");
        return ResponseEntity.status(401).body(error);
    }
}


    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}

// package com.first.first_app.Controller;

// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;

// import com.first.first_app.DTO.LoginRequest;
// import com.first.first_app.Model.User;

// import com.first.first_app.Service.UserService;
// import com.first.first_app.config.CustomUserDetailsService;


// @RestController
// public class UserController {
  
//   @Autowired
//   private UserService userService;
 
//     @Autowired 
//     private AuthenticationManager authenticationManager;
    
//     @Autowired 
//     private CustomUserDetailsService userDetailsService;
    



  
//     /**
//      * Handles user login, authenticates credentials, and issues a JWT token.
//      */
//     @PostMapping("/login")
//     public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) {
        
//         // 1. Authenticate Credentials
//         try {
//             authenticationManager.authenticate(
//                 new UsernamePasswordAuthenticationToken(
//                     authenticationRequest.getEmail(),
//                     authenticationRequest.getPassword()
//                 )
//             );
//         } catch (Exception e) {
//             // Authentication failed (wrong password/email)
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Incorrect email or password"));
//         }

//         // 2. Load User Details & Get User Object for ID/Role
//         // Load UserDetails for token generation (contains roles)
//         final UserDetails userDetails = userDetailsService
//                 .loadUserByUsername(authenticationRequest.getEmail());
                
//         // Load the full User object to get the ID and clean role name (e.g., ADMIN, not ROLE_ADMIN)
//         User loggedInUser = userService.getUserByEmail(authenticationRequest.getEmail()); 
//         Integer userId = loggedInUser.getId(); 
//         String userRole = loggedInUser.getRole().name(); 

        
//         // 4. Return the JWT and User info
//         Map<String, Object> response = new HashMap<>();
     
//         response.put("role", userRole); // e.g., "ADMIN"
//         response.put("userId", userId); 

//         return ResponseEntity.ok(response);
//     }
    
//     // ... (getAllUsers method remains the same) ...

//     @GetMapping("/users")
//     public List<User> getAllUsers() {
//         return userService.getAllUsers();
//     }

// }
