package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {

    public Integer age = 12;
    public String workplace = "KBTU";

    public String giveMyName(String name){
        return "Your age is: " + age + '\n' + "Your workplace: " + workplace;
    }

    public String getMyName(String name){
        return "Your name is: " + name;
    }
}
