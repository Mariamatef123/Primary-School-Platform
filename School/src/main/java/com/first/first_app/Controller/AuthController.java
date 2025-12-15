// package com.first.first_app.Controller;

// import java.util.Map;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.first.first_app.DTO.LoginRequest;

// @RestController
// @RequestMapping("/login")
// public class AuthController {

//     private final AuthenticationManager authManager;

//     public AuthController(AuthenticationManager authManager) {
//         this.authManager = authManager;
//     }

 
//     public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//         try {
//             Authentication auth = authManager.authenticate(
//                 new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//             );

//             return ResponseEntity.ok(Map.of(
//                 "email", auth.getName(),
//                 "roles", auth.getAuthorities().stream()
//                              .map(GrantedAuthority::getAuthority)
//                              .toList()
//             ));
//         } catch (AuthenticationException e) {
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                                  .body(Map.of("error", "Invalid credentials"));
//         }
//     }
// }
