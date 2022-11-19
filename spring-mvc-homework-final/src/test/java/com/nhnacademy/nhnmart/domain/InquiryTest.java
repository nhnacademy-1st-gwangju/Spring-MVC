package com.nhnacademy.nhnmart.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class InquiryTest {

    private Inquiry inquiry;

    private final String INPUT_TITLE = "문의 제목 테스트";
    private final String INPUT_CATEGORY = "문의 카테고리 테스트";
    private final String INPUT_CONTENT = "문의 내용 테스트";
    private final String INPUT_AUTHOR = "Ramos";

    @BeforeEach
    void setUp() {
        inquiry = Inquiry.create(INPUT_TITLE, INPUT_CATEGORY, INPUT_CONTENT, INPUT_AUTHOR);
    }

    @Test
    @DisplayName("Inquiry 생성 후 값 검증")
    void createInquiry_success() {
        assertThat(inquiry.getTitle()).isEqualTo(INPUT_TITLE);
        assertThat(inquiry.getCategory()).isEqualTo(INPUT_CATEGORY);
        assertThat(inquiry.getContent()).isEqualTo(INPUT_CONTENT);
        assertThat(inquiry.getAuthor()).isEqualTo(INPUT_AUTHOR);
        assertThat(inquiry.getImageFiles()).isEmpty();
        assertThat(inquiry.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}