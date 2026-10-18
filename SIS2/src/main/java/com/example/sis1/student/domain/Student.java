package com.example.sis1.student.domain;

import java.util.Objects;

public final class Student {
    private final String studentId;
    private final String firstName;
    private final String lastName;

    public Student(String studentId, String firstName, String lastName) {
        validate(studentId, "studentId", "Student ID", 20);
        validate(firstName, "firstName", "First name", 50);
        validate(lastName, "lastName", "Last name", 50);
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student rename(String firstName, String lastName) {
        return new Student(studentId, firstName, lastName);
    }

    public String studentId() { return studentId; }
    public String firstName() { return firstName; }
    public String lastName() { return lastName; }

    private static void validate(String value, String field, String label, int max) {
        if (value == null || value.isBlank()) {
            throw new InvalidStudentException(field, label + " is required");
        }
        if (value.length() > max) {
            throw new InvalidStudentException(field, label + " must be at most " + max + " characters");
        }
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof Student student && studentId.equals(student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
