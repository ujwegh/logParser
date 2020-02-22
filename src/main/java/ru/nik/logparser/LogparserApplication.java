package ru.nik.logparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LogparserApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogparserApplication.class, args);
    }

}
