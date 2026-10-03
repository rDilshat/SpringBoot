package com.example.sis1.controller;

import com.example.sis1.dto.CreateStudentRequest;
import com.example.sis1.dto.StudentResponse;
import com.example.sis1.dto.UpdateStudentRequest;
import com.example.sis1.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody CreateStudentRequest request) {
        StudentResponse response = StudentResponse.from(studentService.create(request));
        URI location = URI.create("/api/v1/students/" + response.studentId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<StudentResponse> findAll() {
        return studentService.findAll().stream().map(StudentResponse::from).toList();
    }

    @GetMapping("/{studentId}")
    public StudentResponse findById(@PathVariable String studentId) {
        return StudentResponse.from(studentService.findById(studentId));
    }

    @PutMapping("/{studentId}")
    public StudentResponse update(
            @PathVariable String studentId,
            @Valid @RequestBody UpdateStudentRequest request
    ) {
        return StudentResponse.from(studentService.update(studentId, request));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> delete(@PathVariable String studentId) {
        studentService.delete(studentId);
        return ResponseEntity.noContent().build();
    }
}
