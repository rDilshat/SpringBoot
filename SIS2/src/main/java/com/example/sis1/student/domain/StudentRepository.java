package com.example.sis1.student.domain;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Optional<Student> findById(String studentId);
    List<Student> findAll();
    Student insert(Student student);
    Student update(Student student);
    boolean deleteById(String studentId);
}
