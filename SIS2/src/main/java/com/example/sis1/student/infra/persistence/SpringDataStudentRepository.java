package com.example.sis1.student.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataStudentRepository extends JpaRepository<StudentEntity, String> {
    // Assigned IDs must not let POST silently merge into an existing student.
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            INSERT INTO students (student_id, first_name, last_name)
            VALUES (:id, :firstName, :lastName)
            ON CONFLICT (student_id) DO NOTHING
            """, nativeQuery = true)
    int insertStudent(@Param("id") String id, @Param("firstName") String firstName,
                      @Param("lastName") String lastName);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE StudentEntity s SET s.firstName = :firstName, s.lastName = :lastName WHERE s.studentId = :id")
    int updateStudent(@Param("id") String id, @Param("firstName") String firstName,
                      @Param("lastName") String lastName);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM StudentEntity s WHERE s.studentId = :id")
    int deleteStudent(@Param("id") String id);
}
