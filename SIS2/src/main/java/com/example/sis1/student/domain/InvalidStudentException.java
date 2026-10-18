package com.example.sis1.student.domain;

public class InvalidStudentException extends RuntimeException {
    private final String field;

    public InvalidStudentException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String field() {
        return field;
    }
}
