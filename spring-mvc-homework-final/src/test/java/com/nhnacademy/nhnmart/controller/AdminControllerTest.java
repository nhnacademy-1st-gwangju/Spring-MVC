package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.Inquiry;
import com.nhnacademy.nhnmart.exception.InquiryNotFoundException;
import com.nhnacademy.nhnmart.exception.ValidationFailedException;
import com.nhnacademy.nhnmart.repository.InquiryRepository;
import com.nhnacademy.nhnmart.repository.InquiryRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AdminControllerTest {

    private MockMvc mockMvc;

    private InquiryRepository inquiryRepository;

    @BeforeEach
    void setUp() {
        inquiryRepository = mock(InquiryRepositoryImpl.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new AdminController(inquiryRepository))
                .setControllerAdvice(WebControllerAdvice.class)
                .build();
    }

    @Test
    @DisplayName("category query parameter 없이 adminMain 페이지를 반환한다.")
    void adminMain_withOutCategory() throws Exception {
        mockMvc.perform(get("/cs/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminMain"));
    }

    @Test
    @DisplayName("query parameter로 받아온 category로 분류된 리스트와 함께 adminMain 페이지를 반환한다.")
    void adminMain_withCategory() throws Exception {
        String category = "category";

        mockMvc.perform(get("/cs/admin")
                        .param("category", category))
                .andExpect(status().isOk())
                .andExpect(view().name("adminMain"));
    }

    @Test
    @DisplayName("query parameter로 받아온 inquiry번호에 대해 answerRegister 페이지를 반환한다.")
    void answerForm() throws Exception {
        long inquiryId = 1L;
        mockMvc.perform(get("/cs/admin/{inquiryId}/answer", inquiryId))
                .andExpect(status().isOk())
                .andExpect(view().name("answerRegister"));
    }

    @Test
    @DisplayName("문의에 대한 상세 조회 시 inquiryView 페이지를 반환한다.")
    void getUnansweredInquiry_success() throws Exception {
        String inputTitle = "title";
        String inputCategory = "category";
        String inputContent = "content";
        String inputAuthor = "Ramos";

        Inquiry inquiry = Inquiry.create(inputTitle, inputCategory, inputContent, inputAuthor);
        long inquiryId = 1L;
        inquiry.setId(inquiryId);

        when(inquiryRepository.getInquiry(inquiryId)).thenReturn(inquiry);

        MvcResult mvcResult = mockMvc.perform(get("/cs/admin/{inquiryId}", inquiryId))
                .andExpect(status().isOk())
                .andExpect(view().name("inquiryView"))
                .andReturn();

        Optional<Inquiry> viewInquiry = Optional.ofNullable(mvcResult.getModelAndView())
                .map(ModelAndView::getModel)
                .map(m -> m.get("inquiry"))
                .map(Inquiry.class::cast);

        assertThat(viewInquiry).isPresent();
        assertThat(viewInquiry.get().getId()).isEqualTo(inquiryId);
        assertThat(viewInquiry.get().getTitle()).isEqualTo(inputTitle);
        assertThat(viewInquiry.get().getCategory()).isEqualTo(inputCategory);
        assertThat(viewInquiry.get().getContent()).isEqualTo(inputContent);
        assertThat(viewInquiry.get().getAuthor()).isEqualTo(inputAuthor);
    }

    @Test
    @DisplayName("문의에 대한 상세 조회 시 존재하지 않는 문의일 경우 예외를 반환한다.")
    void getUnansweredInquiry_fail_throwInquiryNotFoundException() throws Exception {
        final long invalidInquiryId = 1000L;

        when(inquiryRepository.getInquiry(invalidInquiryId)).thenReturn(null);

        mockMvc.perform(get("/cs/admin/{inquiryId}", invalidInquiryId))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InquiryNotFoundException));
    }

    @Test
    @DisplayName("입력한 답변 내용에 대해 유효성 검증 실패 시 예외를 반환한다.")
    void registerAnswer_fail_throwValidationFailedException() throws Exception {
        long inquiryId = 1L;
        String inputComment = "";
        String adminName = "";

        mockMvc.perform(post("/cs/admin/{inquiryId}/answer", inquiryId)
                .param("content", inputComment)
                .param("adminName", adminName))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationFailedException));
    }

    @Test
    @DisplayName("문의글에 대해 답변을 성공적으로 등록한 뒤 상세 문의 페이지를 반환한다.")
    void registerAnswer_success() throws Exception {
        long inquiryId = 1L;
        String inputComment = "comment";
        String adminName = "Admin";

        mockMvc.perform(post("/cs/admin/{inquiryId}/answer", inquiryId)
                        .param("content", inputComment)
                        .param("adminName", adminName))
                .andExpect(status().isOk())
                .andExpect(view().name("inquiryView"));
    }
}