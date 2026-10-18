package com.example.sis1.student.web;

import com.example.sis1.student.app.StudentService;
import com.example.sis1.student.web.dto.CreateStudentRequest;
import com.example.sis1.student.web.dto.StudentResponse;
import com.example.sis1.student.web.dto.UpdateStudentRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;
    private final StudentApiMapper mapper;

    public StudentController(StudentService studentService, StudentApiMapper mapper) {
        this.studentService = studentService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody CreateStudentRequest request) {
        StudentResponse response = mapper.toResponse(studentService.create(mapper.toCommand(request)));
        URI location = UriComponentsBuilder.fromPath("/api/v1/students/{studentId}")
                .buildAndExpand(response.studentId()).encode().toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<StudentResponse> findAll() {
        return studentService.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{studentId}")
    public StudentResponse findById(@PathVariable String studentId) {
        return mapper.toResponse(studentService.findById(studentId));
    }

    @PutMapping("/{studentId}")
    public StudentResponse update(@PathVariable String studentId,
                                  @Valid @RequestBody UpdateStudentRequest request) {
        return mapper.toResponse(studentService.update(studentId, mapper.toCommand(request)));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> delete(@PathVariable String studentId) {
        studentService.delete(studentId);
        return ResponseEntity.noContent().build();
    }
}
