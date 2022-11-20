package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.exception.UserNotFoundException;
import com.nhnacademy.nhnmart.exception.ValidationFailedException;
import com.nhnacademy.nhnmart.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.nhnacademy.nhnmart.domain.RoleUser.ROLE_ADMIN;
import static com.nhnacademy.nhnmart.domain.RoleUser.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class LoginControllerTest {

    private MockMvc mockMvc;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new LoginController(userRepository))
                .setControllerAdvice(WebControllerAdvice.class)
                .build();
    }

    @Test
    @DisplayName("이미 로그인이 되어 있는 경우 사용자 권한별 메인 페이지를 반환한다.")
    void login_alreadyLogon() throws Exception {
        String session = "SESSION";
        String sessionValue = "sessionValue";

        mockMvc.perform(get("/cs/login")
                        .sessionAttr(session, sessionValue))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cs"));
    }

    @Test
    @DisplayName("로그인이 되어있지 않은 경우 loginForm 페이지를 반환한다.")
    void login_viewLoginForm() throws Exception {
        mockMvc.perform(get("/cs/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginForm"));
    }

    @Test
    @DisplayName("Id/Password가 맞지 않는 경우 login 실패")
    void doLogin_fail_notMatchedIdAndPassword_throwUserNotFoundException() throws Exception {
        String invalidId = "invalidId";
        String invalidPassword = "invalidPassword";

        when(userRepository.matches(invalidId, invalidPassword)).thenReturn(false);

        mockMvc.perform(post("/cs/login")
                .param("id", invalidId)
                .param("pwd", invalidPassword))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));
    }

    @Test
    @DisplayName("입력한 Id/Password에 대한 유효성 검증 실패 시 login 실패")
    void doLogin_fail_whenInputEmptyIdAndPassword_throwValidationFailedException() throws Exception {
        String invalidId = "";
        String invalidPassword = "";

        mockMvc.perform(post("/cs/login")
                        .param("id", invalidId)
                        .param("pwd", invalidPassword))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationFailedException));
    }

    @Test
    @DisplayName("일반 사용자에 대한 로그인 성공 시 일반 사용자의 홈으로 이동한다.")
    void doLogin_success_whenRoleUser() throws Exception {
        String inputId = "id";
        String inputPassword = "pwd";
        String inputName = "name";

        User user = User.create(inputId, inputPassword, ROLE_USER, inputName);

        when(userRepository.matches(inputId, inputPassword)).thenReturn(true);
        when(userRepository.getUser(inputId)).thenReturn(user);

        mockMvc.perform(post("/cs/login")
                .param("id", inputId)
                .param("pwd", inputPassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cs"));
    }

    @Test
    @DisplayName("관리자에 대한 로그인 성공 시 관리자 홈으로 이동한다.")
    void doLogin_success_whenRoleAdmin() throws Exception {
        String inputId = "id";
        String inputPassword = "pwd";
        String inputName = "name";

        User user = User.create(inputId, inputPassword, ROLE_ADMIN, inputName);

        when(userRepository.matches(inputId, inputPassword)).thenReturn(true);
        when(userRepository.getUser(inputId)).thenReturn(user);

        mockMvc.perform(post("/cs/login")
                        .param("id", inputId)
                        .param("pwd", inputPassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cs/admin"));
    }

    @Test
    @DisplayName("로그아웃 성공 시 index 페이지를 반환한다.")
    void logout() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/cs/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}