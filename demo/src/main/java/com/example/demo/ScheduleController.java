package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/StudentSchedule")
@RequiredArgsConstructor

public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping
    public Object Schedule(@RequestParam String studentID){
        if(studentID.equals("23B031409")) {
            return scheduleService.getSchedule();
        }
        else{
            return "There is no schedule for this student";
        }
    }
}
