package com.example.demo;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class ScheduleService {
    public Map<String, ArrayList<String[]>> schedule = new HashMap<>();

    public ScheduleService(){
        ArrayList<String[]> mondaySubjects = new ArrayList<>();
        ArrayList<String[]> saturdaySubjects = new ArrayList<>();

        mondaySubjects.add(new String[]{"Web Programming (Lecture)", "8:00-10:00"});
        mondaySubjects.add(new String[]{"Business English (Lecture)", "11:00-12:00"});

        saturdaySubjects.add(new String[]{"Backend Framework. Spring (Lecture)", "13:00-15:00"});

        schedule.put("Monday", mondaySubjects);
        schedule.put("Saturday", saturdaySubjects);
    }

    public Map<String, ArrayList<String[]>> getSchedule(){
        return schedule;
    }
}
