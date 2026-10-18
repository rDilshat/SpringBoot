package com.example.sis1.student.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StudentTest {
    @Test
    void rejectsBlankName() {
        assertThatThrownBy(() -> new Student("S-1", " ", "Lovelace"))
                .isInstanceOf(InvalidStudentException.class)
                .hasMessage("First name is required");
    }

    @Test
    void renamePreservesIdentity() {
        Student student = new Student("S-1", "Ada", "Lovelace");
        Student renamed = student.rename("Grace", "Hopper");

        assertThat(renamed.studentId()).isEqualTo("S-1");
        assertThat(renamed.firstName()).isEqualTo("Grace");
        assertThat(renamed.lastName()).isEqualTo("Hopper");
    }
}
