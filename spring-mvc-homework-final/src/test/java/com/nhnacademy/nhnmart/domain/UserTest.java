package com.nhnacademy.nhnmart.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.nhnacademy.nhnmart.domain.RoleUser.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User user;

    private final String INPUT_ID = "Ramos";
    private final String INPUT_PW = "test_password";
    private final String INPUT_NAME = "Ramos";

    @BeforeEach
    void setUp() {
        user = User.create(INPUT_ID, INPUT_PW, ROLE_USER, INPUT_NAME);
    }

    @Test
    @DisplayName("User 생성 후 값 검증")
    void createUser_success() {
        assertThat(user.getId()).isEqualTo(INPUT_ID);
        assertThat(user.getPassword()).isEqualTo(INPUT_PW);
        assertThat(user.getRole()).isEqualTo(ROLE_USER);
        assertThat(user.getName()).isEqualTo(INPUT_NAME);
    }
}