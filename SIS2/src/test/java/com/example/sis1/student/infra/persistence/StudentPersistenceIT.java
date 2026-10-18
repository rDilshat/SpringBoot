package com.example.sis1.student.infra.persistence;

import com.example.sis1.student.domain.Student;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=validate")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({JpaStudentRepository.class, StudentEntityMapper.class})
class StudentPersistenceIT {
    @Autowired
    private JpaStudentRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void migratesRealPostgresAndReadsStudentAfterClearingPersistenceContext() {
        assertThat(jdbc.queryForObject("SELECT version()", String.class)).contains("PostgreSQL");
        assertThat(jdbc.queryForObject(
                "SELECT count(*) FROM flyway_schema_history WHERE version = '1' AND success", Integer.class))
                .isEqualTo(1);
        repository.insert(new Student("IT-1", "Ада", "Лавлейс"));
        entityManager.flush();
        entityManager.clear();

        Student loaded = repository.findById("IT-1").orElseThrow();
        assertThat(loaded.firstName()).isEqualTo("Ада");
        assertThat(loaded.lastName()).isEqualTo("Лавлейс");
    }

}
