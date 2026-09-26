package com.example.practice3.service;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MainService {

    private final Optional<FeatureService> featureService;

    public MainService(Optional<FeatureService> featureService) {
        this.featureService = featureService;
    }

    public void run() {

        if (featureService.isPresent()) {
            System.out.println(featureService.get().getMessage());
        } else {
            System.out.println("Conditional feature is disabled!");
        }
    }
}