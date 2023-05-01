package com.fu.lhm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class LhmApplication {
    public static void main(String[] args) {
        SpringApplication.run(LhmApplication.class, args);
    }
}
