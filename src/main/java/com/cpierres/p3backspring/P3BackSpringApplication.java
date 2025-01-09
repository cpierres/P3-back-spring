package com.cpierres.p3backspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class P3BackSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(P3BackSpringApplication.class, args);
    }

}
