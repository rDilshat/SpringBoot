package com.example.sis1.student.infra.persistence;

import com.example.sis1.student.domain.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentEntityMapper {
    public Student toDomain(StudentEntity entity) {
        return new Student(entity.getStudentId(), entity.getFirstName(), entity.getLastName());
    }

    public StudentEntity toEntity(Student student) {
        return new StudentEntity(student.studentId(), student.firstName(), student.lastName());
    }
}
