package com.example.pointmanagementproject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * java -jar batch.jar
 */
@Slf4j
@SpringBootApplication
public class PointManagementProjectApplication {

    public static void main(String[] args) {
        log.info("Application arguments : {}", String.join(", ", args));
        SpringApplication.run(PointManagementProjectApplication.class, args);
    }

}
