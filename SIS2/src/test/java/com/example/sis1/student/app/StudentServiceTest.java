package com.example.sis1.student.app;

import com.example.sis1.student.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    private final Student student = new Student("S-1", "Ada", "Lovelace");

    @Test
    void createsStudentThroughRepository() {
        when(repository.insert(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student result = service.create(new CreateStudentCommand("S-1", "Ada", "Lovelace"));

        assertThat(result.studentId()).isEqualTo("S-1");
        assertThat(result.firstName()).isEqualTo("Ada");
        assertThat(result.lastName()).isEqualTo("Lovelace");
        verify(repository).insert(any(Student.class));
    }

    @Test
    void rejectsDuplicateWithoutWriting() {
        when(repository.findById("S-1")).thenReturn(Optional.of(student));
        assertThatThrownBy(() -> service.create(new CreateStudentCommand("S-1", "Other", "Name")))
                .isInstanceOf(StudentAlreadyExistsException.class);
        verify(repository, never()).insert(any());
    }

    @Test
    void rejectsMissingStudent() {
        assertThatThrownBy(() -> service.findById("missing"))
                .isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void updatesNamesAndKeepsId() {
        when(repository.findById("S-1")).thenReturn(Optional.of(student));
        when(repository.update(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student result = service.update("S-1", new UpdateStudentCommand("Grace", "Hopper"));

        ArgumentCaptor<Student> saved = ArgumentCaptor.forClass(Student.class);
        verify(repository).update(saved.capture());
        assertThat(saved.getValue().studentId()).isEqualTo("S-1");
        assertThat(result.firstName()).isEqualTo("Grace");
        assertThat(result.lastName()).isEqualTo("Hopper");
    }

    @Test
    void deletesExistingStudent() {
        when(repository.deleteById("S-1")).thenReturn(true);
        assertThatCode(() -> service.delete("S-1")).doesNotThrowAnyException();
        verify(repository).deleteById("S-1");
    }

}
