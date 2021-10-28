package com.finalproject.backend.baseballmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class BaseballmateApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseballmateApplication.class, args);
    }

}