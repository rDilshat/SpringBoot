package com.example.sis1.student.infra.persistence;

import com.example.sis1.student.domain.Student;
import com.example.sis1.student.domain.StudentAlreadyExistsException;
import com.example.sis1.student.domain.StudentNotFoundException;
import com.example.sis1.student.domain.StudentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaStudentRepository implements StudentRepository {
    private final SpringDataStudentRepository jpa;
    private final StudentEntityMapper mapper;

    public JpaStudentRepository(SpringDataStudentRepository jpa, StudentEntityMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Optional<Student> findById(String studentId) {
        return jpa.findById(studentId).map(mapper::toDomain);
    }

    @Override
    public List<Student> findAll() {
        return jpa.findAll(Sort.by("studentId")).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Student insert(Student student) {
        StudentEntity entity = mapper.toEntity(student);
        if (jpa.insertStudent(entity.getStudentId(), entity.getFirstName(), entity.getLastName()) == 0) {
            throw new StudentAlreadyExistsException(student.studentId());
        }
        return mapper.toDomain(entity);
    }

    @Override
    public Student update(Student student) {
        StudentEntity entity = mapper.toEntity(student);
        if (jpa.updateStudent(entity.getStudentId(), entity.getFirstName(), entity.getLastName()) == 0) {
            throw new StudentNotFoundException(student.studentId());
        }
        return mapper.toDomain(entity);
    }

    @Override
    public boolean deleteById(String studentId) {
        return jpa.deleteStudent(studentId) > 0;
    }
}
