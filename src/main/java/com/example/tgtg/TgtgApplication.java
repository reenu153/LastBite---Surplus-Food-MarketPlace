package com.example.tgtg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// LEARN: @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan.
// Component scan starts from THIS package, so all other classes must live under com.example.tgtg.
@SpringBootApplication
public class TgtgApplication {

    public static void main(String[] args) {
        SpringApplication.run(TgtgApplication.class, args);
    }
}
