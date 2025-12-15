// package com.first.first_app.config;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.filter.OncePerRequestFilter;

// import java.io.IOException;
// import java.util.List;

// public class JwtFilter extends OncePerRequestFilter {

//     private static final String SECRET = "secret123"; // Your secret key

//     @Override
//     protected void doFilterInternal(HttpServletRequest request,
//                                     HttpServletResponse response,
//                                     FilterChain filterChain)
//             throws ServletException, IOException {

//         String header = request.getHeader("Authorization");

//         if (header != null && header.startsWith("Bearer ")) {
//             try {
//                 String token = header.substring(7);
//                 Claims claims = Jwts.parser()
//                         .setSigningKey(SECRET)
//                         .parseClaimsJws(token)
//                         .getBody();

//                 String email = claims.getSubject();
//                 String role = claims.get("role", String.class);

//                 UsernamePasswordAuthenticationToken auth =
//                         new UsernamePasswordAuthenticationToken(
//                                 email,
//                                 null,
//                                 List.of(new SimpleGrantedAuthority("ROLE_" + role))
//                         );

//                 SecurityContextHolder.getContext().setAuthentication(auth);

//             } catch (Exception e) {
//                 SecurityContextHolder.clearContext(); // Allow anonymous if invalid token
//             }
//         }

//         filterChain.doFilter(request, response);
//     }
// }
