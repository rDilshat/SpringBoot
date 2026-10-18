CREATE TABLE students (
    student_id VARCHAR(20) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    CONSTRAINT students_id_not_blank CHECK (student_id ~ '[^[:space:]]'),
    CONSTRAINT students_first_name_not_blank CHECK (first_name ~ '[^[:space:]]'),
    CONSTRAINT students_last_name_not_blank CHECK (last_name ~ '[^[:space:]]')
);
