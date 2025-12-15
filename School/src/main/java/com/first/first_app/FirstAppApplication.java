package com.first.first_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EntityScan
@SpringBootApplication
public class FirstAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstAppApplication.class, args);
    }
}
