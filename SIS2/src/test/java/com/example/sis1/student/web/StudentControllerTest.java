package com.example.sis1.student.web;

import com.example.sis1.student.app.CreateStudentCommand;
import com.example.sis1.student.app.StudentService;
import com.example.sis1.student.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import({StudentApiMapper.class, GlobalExceptionHandler.class})
class StudentControllerTest {
    private static final String URL = "/api/v1/students";
    private static final String VALID_CREATE = """
            {"studentId":"S-1","firstName":"Ada","lastName":"Lovelace"}
            """;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private StudentService service;

    private final Student student = new Student("S-1", "Ada", "Lovelace");

    @Test
    void createsStudentWithLocationAndDto() throws Exception {
        when(service.create(new CreateStudentCommand("S-1", "Ada", "Lovelace"))).thenReturn(student);
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(VALID_CREATE))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", URL + "/S-1"))
                .andExpect(jsonPath("$.studentId").value("S-1"))
                .andExpect(jsonPath("$.firstName").value("Ada"))
                .andExpect(jsonPath("$.lastName").value("Lovelace"));
        verify(service).create(new CreateStudentCommand("S-1", "Ada", "Lovelace"));
    }

    @Test
    void returns404ForMissingStudent() throws Exception {
        when(service.findById("missing")).thenThrow(new StudentNotFoundException("missing"));
        mvc.perform(get(URL + "/missing")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("STUDENT_NOT_FOUND"))
                .andExpect(jsonPath("$.path").value(URL + "/missing"));
    }

    @Test
    void returns409ForDuplicate() throws Exception {
        when(service.create(any())).thenThrow(new StudentAlreadyExistsException("S-1"));
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(VALID_CREATE))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("STUDENT_ALREADY_EXISTS"));
    }

    @Test
    void returnsFieldErrorsForBlankRequestWithoutCallingService() throws Exception {
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\":\"\",\"firstName\":\" \",\"lastName\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.fieldErrors.studentId").exists())
                .andExpect(jsonPath("$.fieldErrors.firstName").exists())
                .andExpect(jsonPath("$.fieldErrors.lastName").exists());
        verifyNoInteractions(service);
    }

}
