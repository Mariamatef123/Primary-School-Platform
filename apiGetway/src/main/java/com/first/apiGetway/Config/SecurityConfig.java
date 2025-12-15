package com.first.apiGetway.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http
            // Disable CSRF for REST/Gateway
            .csrf(csrf -> csrf.disable())

            // All requests are permitted (no login required)
            .authorizeExchange(auth -> auth
                .pathMatchers("/**").permitAll()
            )

            // Disable default login forms
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(form -> form.disable());

        return http.build();
    }
}
