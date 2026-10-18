package com.example.sis1.student.app;

import com.example.sis1.student.domain.Student;
import com.example.sis1.student.domain.StudentAlreadyExistsException;
import com.example.sis1.student.domain.StudentNotFoundException;
import com.example.sis1.student.domain.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository students;

    public StudentService(StudentRepository students) {
        this.students = students;
    }

    @Transactional
    public Student create(CreateStudentCommand command) {
        Student student = new Student(command.studentId(), command.firstName(), command.lastName());
        if (students.findById(student.studentId()).isPresent()) {
            throw new StudentAlreadyExistsException(student.studentId());
        }
        return students.insert(student);
    }

    public List<Student> findAll() {
        return students.findAll();
    }

    public Student findById(String studentId) {
        return students.findById(studentId).orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    @Transactional
    public Student update(String studentId, UpdateStudentCommand command) {
        Student student = findById(studentId);
        return students.update(student.rename(command.firstName(), command.lastName()));
    }

    @Transactional
    public void delete(String studentId) {
        if (!students.deleteById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
    }
}
