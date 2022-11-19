package com.nhnacademy.nhnmart.repository;

import com.nhnacademy.nhnmart.domain.Answer;
import com.nhnacademy.nhnmart.domain.Inquiry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InquiryRepositoryImplTest {

    private InquiryRepository inquiryRepository;

    private final String INPUT_TITLE = "inputTitle";
    private final String INPUT_CATEGORY = "inputCategory";
    private final String INPUT_CONTENT = "inputContent";
    private final String INPUT_AUTHOR = "inputAuthor";
    private final List<String> INPUT_UPLOAD_FILE_DIRS = new ArrayList<>();

    @BeforeEach
    void setUp() {
        inquiryRepository = new InquiryRepositoryImpl();
    }

    @Test
    @DisplayName("inquiryRepository 등록되지 않은 문의는 조회 시 false를 반환한다.")
    void exists_fail() {
        //given
        int invalidInquiryId = 100;

        //when
        boolean checkExists = inquiryRepository.exists(invalidInquiryId);

        //then
        assertThat(checkExists).isFalse();
    }

    @Test
    @DisplayName("InquiryRepository 등록된 문의는 조회 시 true를 반환한다.")
    void exists_success() {
        //given
        inquiryRepository.registerInquiry(INPUT_TITLE, INPUT_CATEGORY, INPUT_CONTENT, INPUT_AUTHOR, INPUT_UPLOAD_FILE_DIRS);
        long inquiryId = 1L;

        //when
        boolean checkExists = inquiryRepository.exists(inquiryId);

        //then
        assertThat(checkExists).isTrue();
    }

    @Test
    @DisplayName("InquiryRepository에 문의가 등록된 경우 id 값이 auto_increment 방식으로 세팅되며 정상적으로 저장된다.")
    void registerInquiry_success() {
        //given
        long inquiryId = 1L;

        //when
        Inquiry inquiry = inquiryRepository.registerInquiry(INPUT_TITLE, INPUT_CATEGORY, INPUT_CONTENT, INPUT_AUTHOR, INPUT_UPLOAD_FILE_DIRS);

        //then
        assertThat(inquiry.getId()).isEqualTo(inquiryId);
        assertThat(inquiry.getTitle()).isEqualTo(INPUT_TITLE);
        assertThat(inquiry.getCategory()).isEqualTo(INPUT_CATEGORY);
        assertThat(inquiry.getContent()).isEqualTo(INPUT_CONTENT);
        assertThat(inquiry.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(inquiry.getAuthor()).isEqualTo(INPUT_AUTHOR);
        assertThat(inquiry.getImageFiles()).isEmpty();
    }

    @Test
    @DisplayName("InquiryRepository에 등록되지 않은 문의를 불러올 경우 null을 반환한다.")
    void getInquiry_fail() {
        //given
        long inquiryId = 1L;

        //when
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);

        //then
        assertThat(inquiry).isNull();
    }

    @Test
    @DisplayName("InquiryRepository에 등록된 문의를 불러올 경우 Inquiry 조회가 성공한다.")
    void getInquiry_success() {
        //given
        long inquiryId = 1L;
        inquiryRepository.registerInquiry(INPUT_TITLE, INPUT_CATEGORY, INPUT_CONTENT, INPUT_AUTHOR, INPUT_UPLOAD_FILE_DIRS);

        //when
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);

        //then
        assertThat(inquiry.getId()).isEqualTo(inquiryId);
        assertThat(inquiry.getTitle()).isEqualTo(INPUT_TITLE);
        assertThat(inquiry.getCategory()).isEqualTo(INPUT_CATEGORY);
        assertThat(inquiry.getContent()).isEqualTo(INPUT_CONTENT);
        assertThat(inquiry.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(inquiry.getAuthor()).isEqualTo(INPUT_AUTHOR);
        assertThat(inquiry.getImageFiles()).isEmpty();
    }

    @Test
    @DisplayName("InquiryRepository에 등록된 모든 문의를 불러온다.")
    void getAllInquiries_success() {
        //given
        int expectedSize = 1;
        inquiryRepository.registerInquiry(INPUT_TITLE, INPUT_CATEGORY, INPUT_CONTENT, INPUT_AUTHOR, INPUT_UPLOAD_FILE_DIRS);

        //when
        List<Inquiry> inquiries = inquiryRepository.getAllInquiries();

        //then
        assertThat(inquiries).hasSize(expectedSize);
    }

    @Test
    @DisplayName("등록되지 않은 문의에 대해 답변을 등록할 경우 NullPointerException을 반환한다.")
    void addAnswer_fail_throwNullPointerException() {
        //given
        String inputAnswerComment = "comment";
        String adminName = "Admin";
        Answer answer = Answer.create(inputAnswerComment, adminName);
        long invalidInquiryId = 1000L;

        //when, then
        assertThatThrownBy(() -> inquiryRepository.addAnswer(invalidInquiryId, answer))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("등록된 문의에 대해 답변 등록 성공")
    void addAnswer_success() {
        //given
        String inputAnswerComment = "comment";
        String adminName = "Admin";
        Answer answer = Answer.create(inputAnswerComment, adminName);

        inquiryRepository.registerInquiry(INPUT_TITLE, INPUT_CATEGORY, INPUT_CONTENT, INPUT_AUTHOR, INPUT_UPLOAD_FILE_DIRS);
        long inquiryId = 1L;

        //when
        Inquiry inquiry = inquiryRepository.addAnswer(inquiryId, answer);

        //then
        assertThat(inquiry.getAnswer().getComment()).isEqualTo(inputAnswerComment);
        assertThat(inquiry.getAnswer().getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(inquiry.getAnswer().getAdminName()).isEqualTo(adminName);
    }

    @Test
    @DisplayName("등록된 모든 문의에 답변이 달려있는 경우 emptyList를 반환한다.")
    void getUnansweredInquiries_return_emptyList() {
        //given
        String inputAnswerComment = "comment";
        String adminName = "Admin";
        Answer answer = Answer.create(inputAnswerComment, adminName);

        inquiryRepository.registerInquiry(INPUT_TITLE, INPUT_CATEGORY, INPUT_CONTENT, INPUT_AUTHOR, INPUT_UPLOAD_FILE_DIRS);
        inquiryRepository.addAnswer(1L, answer);

        //when
        List<Inquiry> unansweredInquiries = inquiryRepository.getUnansweredInquiries();

        //then
        assertThat(unansweredInquiries).isEmpty();
    }

    @Test
    @DisplayName("등록된 문의들 중 답변이 달리지 않은 문의들의 리스트를 반환한다.")
    void getUnansweredInquiries_success() {
        //given
        int expectedSize = 1;
        inquiryRepository.registerInquiry(INPUT_TITLE, INPUT_CATEGORY, INPUT_CONTENT, INPUT_AUTHOR, INPUT_UPLOAD_FILE_DIRS);

        //when
        List<Inquiry> unansweredInquiries = inquiryRepository.getUnansweredInquiries();

        //then
        assertThat(unansweredInquiries).hasSize(expectedSize);
    }

    @Test
    @DisplayName("등록된 문의들의 카테고리와 다른 카테고리로 조회할 경우 emptyList를 반환한다.")
    void findAllByCategory_return_emptyList() {
        //given
        String anotherCategory = "anotherCategory";
        inquiryRepository.registerInquiry(INPUT_TITLE, INPUT_CATEGORY, INPUT_CONTENT, INPUT_AUTHOR, INPUT_UPLOAD_FILE_DIRS);

        //when
        List<Inquiry> allByCategory = inquiryRepository.findAllByCategory(anotherCategory);

        //then
        assertThat(allByCategory).isEmpty();
    }

    @Test
    void findAllByCategory_success() {
        //given
        int expectedSize = 1;
        int index = 0;
        inquiryRepository.registerInquiry(INPUT_TITLE, INPUT_CATEGORY, INPUT_CONTENT, INPUT_AUTHOR, INPUT_UPLOAD_FILE_DIRS);

        //when
        List<Inquiry> allByCategory = inquiryRepository.findAllByCategory(INPUT_CATEGORY);

        //then
        assertThat(allByCategory).hasSize(expectedSize);
        assertThat(allByCategory.get(index).getCategory()).isEqualTo(INPUT_CATEGORY);
    }
}