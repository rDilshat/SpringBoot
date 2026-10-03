package com.example.sis1.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String studentId) {
        super("Student with ID " + studentId + " was not found");
    }
}
