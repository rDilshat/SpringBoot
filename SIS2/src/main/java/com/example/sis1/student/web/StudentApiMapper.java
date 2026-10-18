package com.example.sis1.student.web;

import com.example.sis1.student.app.CreateStudentCommand;
import com.example.sis1.student.app.UpdateStudentCommand;
import com.example.sis1.student.domain.Student;
import com.example.sis1.student.web.dto.CreateStudentRequest;
import com.example.sis1.student.web.dto.StudentResponse;
import com.example.sis1.student.web.dto.UpdateStudentRequest;
import org.springframework.stereotype.Component;

@Component
public class StudentApiMapper {
    public CreateStudentCommand toCommand(CreateStudentRequest request) {
        return new CreateStudentCommand(request.studentId(), request.firstName(), request.lastName());
    }

    public UpdateStudentCommand toCommand(UpdateStudentRequest request) {
        return new UpdateStudentCommand(request.firstName(), request.lastName());
    }

    public StudentResponse toResponse(Student student) {
        return new StudentResponse(student.studentId(), student.firstName(), student.lastName());
    }
}
