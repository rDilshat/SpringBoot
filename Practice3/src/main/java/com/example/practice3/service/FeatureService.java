package com.example.practice3.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        name = "practice.feature.enabled",
        havingValue = "true"
)
public class FeatureService {

    public String getMessage() {
        return "Conditional feature is enabled!";
    }
}