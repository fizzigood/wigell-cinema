package com.wigell.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Startpunkt för Wigell Cinema-applikationen.
// Spring Boot skannar automatiskt alla klasser i detta paket och underpaket
// (controller, service, entity, repository, config, dto).
@SpringBootApplication
public class CinemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }
}
