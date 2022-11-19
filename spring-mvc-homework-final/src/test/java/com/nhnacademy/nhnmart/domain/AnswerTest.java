package com.nhnacademy.nhnmart.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {

    private Answer answer;

    private final String INPUT_COMMENT = "답변 본문입니다.";
    private final String INPUT_ADMIN_NAME = "관리자";

    @BeforeEach
    void setUp() {
        answer = Answer.create(INPUT_COMMENT, INPUT_ADMIN_NAME);
    }

    @Test
    @DisplayName("Answer 생성 후 값 검증")
    void createAnswer_success() {
        assertThat(answer.getComment()).isEqualTo(INPUT_COMMENT);
        assertThat(answer.getAdminName()).isEqualTo(INPUT_ADMIN_NAME);
        assertThat(answer.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}