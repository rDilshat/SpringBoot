package com.example.sis1.student.domain;

public class StudentAlreadyExistsException extends RuntimeException {

    public StudentAlreadyExistsException(String studentId) {
        super("Student with ID " + studentId + " already exists");
    }
}
