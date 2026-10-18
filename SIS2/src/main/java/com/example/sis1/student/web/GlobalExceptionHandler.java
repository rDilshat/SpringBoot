package com.example.sis1.student.web;

import com.example.sis1.student.domain.InvalidStudentException;
import com.example.sis1.student.domain.StudentAlreadyExistsException;
import com.example.sis1.student.domain.StudentNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            StudentNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.NOT_FOUND, "STUDENT_NOT_FOUND", exception.getMessage(),
                request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleConflict(
            StudentAlreadyExistsException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.CONFLICT, "STUDENT_ALREADY_EXISTS", exception.getMessage(),
                request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage()));

        return buildError(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "Request validation failed",
                request.getRequestURI(), fieldErrors);
    }

    @ExceptionHandler(InvalidStudentException.class)
    public ResponseEntity<ApiError> handleInvalidStudent(
            InvalidStudentException exception, HttpServletRequest request
    ) {
        return buildError(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "Request validation failed",
                request.getRequestURI(), Map.of(exception.field(), exception.getMessage()));
    }

    private ResponseEntity<ApiError> buildError(
            HttpStatus status,
            String error,
            String message,
            String path,
            Map<String, String> fieldErrors
    ) {
        ApiError body = new ApiError(Instant.now(), status.value(), error, message, path, fieldErrors);
        return ResponseEntity.status(status).body(body);
    }
}
