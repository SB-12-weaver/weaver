package com.sbproject.weaver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class WeaverApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeaverApplication.class, args);
        System.out.println("http://localhost:8080");
    }

}
