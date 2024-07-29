package com.example.springsecurity6;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class SpringSecurity6Application {
    private final Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurity6Application.class, args);
    }

//    @Bean
//    public CommandLineRunner runner() {
//        return args -> {
//            Dotenv dotenv = Dotenv.load();
//            String mailUsername = dotenv.get("MAIL_USERNAME");
//            String mailPassword = dotenv.get("MAIL_PASSWORD");
//            System.out.println(mailUsername+" is your email address and "+mailPassword+" is your password");
//        };
//    }
}
