package com.example.practice3.service;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MainService {

    private final Optional<FeatureService> featureService;

    public MainService(Optional<FeatureService> featureService) {
        this.featureService = featureService;
    }

    public String getMessage() {
        if (featureService.isPresent()) {
            return featureService.get().getMessage();
        }

        return "Conditional feature is disabled!";
    }
}