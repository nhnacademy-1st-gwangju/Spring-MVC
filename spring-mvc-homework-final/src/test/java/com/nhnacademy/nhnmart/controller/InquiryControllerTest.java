package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.Inquiry;
import com.nhnacademy.nhnmart.exception.InquiryNotFoundException;
import com.nhnacademy.nhnmart.exception.ValidationFailedException;
import com.nhnacademy.nhnmart.repository.InquiryRepository;
import com.nhnacademy.nhnmart.repository.InquiryRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class InquiryControllerTest {

    private MockMvc mockMvc;

    private InquiryRepository inquiryRepository;

    @BeforeEach
    void setUp() {
        inquiryRepository = mock(InquiryRepositoryImpl.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new InquiryController(inquiryRepository))
                .setControllerAdvice(WebControllerAdvice.class)
                .build();
    }

    @Test
    @DisplayName("userMain 페이지를 반환한다.")
    void userMain() throws Exception {
        mockMvc.perform(get("/cs"))
                .andExpect(status().isOk())
                .andExpect(view().name("userMain"));
    }

    @Test
    @DisplayName("문의 등록을 위한 inquiryRegister 페이지를 반환한다.")
    void inquiryForm_success() throws Exception {
        mockMvc.perform(get("/cs/inquiry"))
                .andExpect(status().isOk())
                .andExpect(view().name("inquiryRegister"));
    }

    @Test
    @DisplayName("입력한 문의 내용들에 대해 유효성 검증 실패 시 예외를 반환한다.")
    void doInquiry_fail_throwValidationFailedException() throws Exception {
        String inputTitle = "";
        String inputCategory = "";
        String inputContent = "";
        String inputAuthor = "";

        mockMvc.perform(post("/cs/inquiry")
                .param("title", inputTitle)
                .param("category", inputCategory)
                .param("content", inputContent)
                .param("author", inputAuthor))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationFailedException));
    }

    @Test
    @DisplayName("문의 등록 시 이미지 파일을 첨부한 경우")
    void doInquiry_success_withImageFiles() throws Exception {
        String inputTitle = "title";
        String inputCategory = "category";
        String inputContent = "content";
        String inputAuthor = "Ramos";

        MockMultipartFile files = new MockMultipartFile("imageFiles", "imagefile.png", "image/png", "<<png data>>".getBytes());

        mockMvc.perform(multipart("/cs/inquiry")
                        .file(files)
                        .param("title", inputTitle)
                        .param("category", inputCategory)
                        .param("content", inputContent)
                        .param("author", inputAuthor))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cs"));
    }

    @Test
    @DisplayName("문의 등록 시 파일을 첨부하지 않은 경우")
    void doInquiry_success_withoutImageFiles() throws Exception {
        String inputTitle = "title";
        String inputCategory = "category";
        String inputContent = "content";
        String inputAuthor = "Ramos";

        MockMultipartFile files = new MockMultipartFile("imageFiles", "", "", "".getBytes());

        mockMvc.perform(multipart("/cs/inquiry")
                        .file(files)
                        .param("title", inputTitle)
                        .param("category", inputCategory)
                        .param("content", inputContent)
                        .param("author", inputAuthor))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cs"));
    }

    @Test
    @DisplayName("찾을 수 없는 문의에 대한 조회 시 예외를 반환한다.")
    void inquiryDetail_fail_throwInquiryNotFoundException() throws Exception {
        long invalidInquiryId = 1000L;

        when(inquiryRepository.getInquiry(invalidInquiryId)).thenReturn(null);

        mockMvc.perform(get("/cs/inquiry/{inquiryId}", invalidInquiryId))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InquiryNotFoundException));
    }

    @Test
    @DisplayName("등록 되어 있는 문의에 대한 상세 페이지를 반환한다.")
    void inquiryDetail_success() throws Exception {
        long inquiryId = 1L;
        String inputTitle = "title";
        String inputCategory = "category";
        String inputContent = "content";
        String inputAuthor = "Ramos";

        Inquiry inquiry = Inquiry.create(inputTitle, inputCategory, inputContent, inputAuthor);
        inquiry.setId(inquiryId);

        when(inquiryRepository.getInquiry(inquiryId)).thenReturn(inquiry);

        mockMvc.perform(get("/cs/inquiry/{inquiryId}", inquiryId))
                .andExpect(status().isOk())
                .andExpect(view().name("inquiryView"));
    }
}