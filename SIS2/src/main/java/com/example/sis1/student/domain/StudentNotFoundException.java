package com.example.sis1.student.domain;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String studentId) {
        super("Student with ID " + studentId + " was not found");
    }
}
