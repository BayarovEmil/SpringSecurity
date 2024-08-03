package com.example.springsecurity6;

import com.example.springsecurity6.model.User;
import com.example.springsecurity6.model.role.Role;
import com.example.springsecurity6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class SpringSecurity6Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurity6Application.class, args);
    }

    @Bean
    public CommandLineRunner runner(
            UserRepository userRepository,
            Environment environment,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            String username = environment.getProperty("ADMIN_USERNAME");
            String email = environment.getProperty("ADMIN_EMAIL");
            String password = environment.getProperty("ADMIN_PASSWORD");

            if (userRepository.findByEmail(email).isEmpty()) {
                userRepository.save(
                   User.builder()
                           .firstname(username)
                           .email(email)
                           .password(passwordEncoder.encode(password))
                           .role(Role.ADMIN)
                           .enabled(true)
                           .build()
                );
            }
        };
    }
}
