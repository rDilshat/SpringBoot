package com.example.sis1.dto;

import com.example.sis1.model.Student;

public record StudentResponse(String studentId, String firstName, String lastName) {

    public static StudentResponse from(Student student) {
        return new StudentResponse(student.studentId(), student.firstName(), student.lastName());
    }
}
