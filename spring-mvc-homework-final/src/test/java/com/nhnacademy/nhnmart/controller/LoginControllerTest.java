package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class LoginControllerTest {

    private MockMvc mockMvc;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new LoginController(userRepository))
                .build();
    }

    @Test
    @DisplayName("이미 로그인이 되어 있는 경우 index view를 반환한다.")
    void login_alreadyLogon() throws Exception {
        String session = "SESSION";
        String sessionValue = "sessionValue";
        Cookie cookie = new Cookie(session, sessionValue);

        mockMvc.perform(get("/cs/login")
                        .cookie(cookie))
                .andExpect(view().name("index"));
    }

    @Test
    void login_viewLoginForm() throws Exception {
        mockMvc.perform(get("/cs/login"))
                .andExpect(view().name("loginForm"));
    }
}