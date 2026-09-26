package com.example.practice3.controller;

import com.example.practice3.service.MainService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Practice3Controller {

    private final MainService mainService;

    public Practice3Controller(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/practice3")
    public String practice() {
        return mainService.getMessage();
    }
}