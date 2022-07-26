package com.bankapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BankApplicationApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankApplicationApplication.class, args);
    }

}
