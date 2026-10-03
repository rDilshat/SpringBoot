package com.example.sis1.service;

import com.example.sis1.dto.CreateStudentRequest;
import com.example.sis1.dto.UpdateStudentRequest;
import com.example.sis1.exception.StudentAlreadyExistsException;
import com.example.sis1.exception.StudentNotFoundException;
import com.example.sis1.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StudentService {

    private final Map<String, Student> students = new ConcurrentHashMap<>();

    public Student create(CreateStudentRequest request) {
        Student student = new Student(request.studentId(), request.firstName(), request.lastName());
        if (students.putIfAbsent(student.studentId(), student) != null) {
            throw new StudentAlreadyExistsException(student.studentId());
        }
        return student;
    }

    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    public Student findById(String studentId) {
        Student student = students.get(studentId);
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        return student;
    }

    public Student update(String studentId, UpdateStudentRequest request) {
        findById(studentId);
        Student updatedStudent = new Student(studentId, request.firstName(), request.lastName());
        students.put(studentId, updatedStudent);
        return updatedStudent;
    }

    public void delete(String studentId) {
        if (students.remove(studentId) == null) {
            throw new StudentNotFoundException(studentId);
        }
    }
}
