// package com.first.first_app.config;

// import com.first.first_app.Model.User;
// import com.first.first_app.Repo.UserRepo;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import java.util.Collections;
// import java.util.Optional;

// @Service
// public class CustomUserDetailsService implements UserDetailsService {

//     @Autowired
//     private UserRepo userRepo;

//     @Override
//     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//         Optional<User> userOptional = Optional.ofNullable(userRepo.findByEmail(email));

//         User user = userOptional.orElseThrow(() ->
//                 new UsernameNotFoundException("User not found with email: " + email)
//         );

//         // Convert database role to Spring Security ROLE_ format
//         String grantedRole = "ROLE_" + user.getRole(); // e.g., "ROLE_ADMIN", "ROLE_PARENT"

//         // *** CHANGE THIS LINE ***
//         // Return an instance of your custom class which includes the ID
//         return new com.first.first_app.Model.CustomUserDetails(
//                 user.getId(), // Pass the ID from your database entity
//                 user.getEmail(),
//                 user.getPassword(),
//                 Collections.singletonList(new SimpleGrantedAuthority(grantedRole))
//         );
//     }
// }
