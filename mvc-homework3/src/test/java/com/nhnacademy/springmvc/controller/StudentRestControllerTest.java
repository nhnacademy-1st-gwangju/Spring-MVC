package com.nhnacademy.springmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.domain.StudentModifyRequest;
import com.nhnacademy.springmvc.domain.StudentRegisterRequest;
import com.nhnacademy.springmvc.exception.StudentNotFoundException;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudentRestControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper mapper;
    private StudentRepository studentRepository;

    private static final String INPUT_NAME = "testName";
    private static final String INPUT_EMAIL = "test@test.com";
    private static final String INPUT_COMMENT = "testComment";
    private static final int INPUT_SCORE = 100;

    private static final String INVALID_NAME = "";
    private static final String INVALID_EMAIL = "invalidEmail";
    private static final int INVALID_SCORE = -1;
    private static final String INVALID_COMMENT = "";

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);

        mockMvc = MockMvcBuilders.standaloneSetup(new StudentRestController(studentRepository))
                .setControllerAdvice(WebControllerAdvice.class)
                .build();
        mapper = new ObjectMapper();
    }

    @Test
    void getStudent_success() throws Exception {
        Student student = Student.create(INPUT_NAME, INPUT_EMAIL, INPUT_SCORE, INPUT_COMMENT);
        student.setId(1L);

        when(studentRepository.getStudent(1L)).thenReturn(student);

        MvcResult mvcResult = mockMvc.perform(get("/students/{studentId}", 1L)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(mapper.writeValueAsString(student));
    }

    @Test
    void getStudent_fail_throwStudentNotFoundException() throws Exception {
        when(studentRepository.getStudent(1L)).thenThrow(StudentNotFoundException.class);

        MvcResult mvcResult = mockMvc.perform(get("/students/{studentId}", 1L)
                        .accept("application/json"))
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void registerStudent_success() throws Exception {
        StudentRegisterRequest request = new StudentRegisterRequest(INPUT_NAME, INPUT_EMAIL, INPUT_SCORE, INPUT_COMMENT);
        Student student = Student.create(INPUT_NAME, INPUT_EMAIL, INPUT_SCORE, INPUT_COMMENT);
        student.setId(1L);

        when(studentRepository.register(INPUT_NAME, INPUT_EMAIL, INPUT_SCORE, INPUT_COMMENT)).thenReturn(student);

        MvcResult mvcResult = mockMvc.perform(post("/students")
                        .accept("application/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(mapper.writeValueAsString(student));
    }

    @Test
    void registerStudent_fail_throwValidationFailedException() throws Exception {
        StudentRegisterRequest request = new StudentRegisterRequest(INPUT_NAME, INVALID_EMAIL, INPUT_SCORE, INPUT_COMMENT);

        MvcResult mvcResult = mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(ValidationFailedException.class);
    }

    @Test
    void modifyStudent_success() throws Exception {
        final String changeName = "changeName";
        StudentModifyRequest request = new StudentModifyRequest(changeName, INPUT_EMAIL, INPUT_SCORE, INPUT_COMMENT);

        Student student = Student.create(INPUT_NAME, INPUT_EMAIL, INPUT_SCORE, INPUT_COMMENT);
        student.setId(1L);
        when(studentRepository.getStudent(1L)).thenReturn(student);

        student.setName(changeName);
        when(studentRepository.modify(student)).thenReturn(student);

        MvcResult mvcResult = mockMvc.perform(put("/students/{studentId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(mapper.writeValueAsString(student));
    }

    @Test
    void modifyStudent_fail_throwValidationFailedException() throws Exception {
        StudentModifyRequest request = new StudentModifyRequest(INVALID_NAME, INVALID_EMAIL, INVALID_SCORE, INVALID_COMMENT);

        MvcResult mvcResult = mockMvc.perform(put("/students/{studentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(ValidationFailedException.class);
    }
}