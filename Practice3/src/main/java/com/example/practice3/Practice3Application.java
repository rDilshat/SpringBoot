package com.example.practice3;

import com.example.practice3.service.MainService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Practice3Application implements CommandLineRunner {

    private final MainService mainService;

    public Practice3Application(MainService mainService) {
        this.mainService = mainService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Practice3Application.class, args);
    }

    @Override
    public void run(String... args) {
        mainService.run();
    }
}