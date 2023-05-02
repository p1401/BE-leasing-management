package com.fu.lhm;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Generated
@SpringBootApplication
@EnableScheduling
public class LhmApplication {
    public static void main(String[] args) {
        SpringApplication.run(LhmApplication.class, args);
    }
}
