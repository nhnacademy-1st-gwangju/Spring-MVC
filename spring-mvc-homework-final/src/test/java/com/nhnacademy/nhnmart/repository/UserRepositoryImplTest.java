package com.nhnacademy.nhnmart.repository;

import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.nhnacademy.nhnmart.domain.RoleUser.ROLE_USER;
import static org.assertj.core.api.Assertions.*;

class UserRepositoryImplTest {

    private UserRepository userRepository;

    private User inputUser;

    private final String INPUT_ID = "Ramos";
    private final String INPUT_PW = "validPassword";
    private final String INPUT_NAME = "Ramos";
    private final String INVALID_ID = "invalidId";
    private final String INVALID_PW = "invalidPassword";


    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl();
        inputUser = User.create(INPUT_ID, INPUT_PW, ROLE_USER, INPUT_NAME);
    }

    @Test
    @DisplayName("UserRepository에 등록되지 않은 사용자는 조회 시 false를 반환한다.")
    void exists_fail_notRegisteredUser() {
        //when
        boolean checkExist = userRepository.exists(INVALID_ID);

        //then
        assertThat(checkExist).isFalse();
    }

    @Test
    @DisplayName("UserRepository에 등록된 사용자는 조회 시 true를 반환한다.")
    void exists_success() {
        //given
        userRepository.addUser(INPUT_ID, INPUT_PW, ROLE_USER, INPUT_NAME);

        //when
        boolean checkExist = userRepository.exists(INPUT_ID);

        //then
        assertThat(checkExist).isTrue();
    }

    @Test
    @DisplayName("UserRepository에 등록된 사용자 정보와 입력된 ID/PW와 일치하지 않는 경우 false를 반환한다.")
    void matches_fail() {
        //given
        userRepository.addUser(INPUT_ID, INPUT_PW, ROLE_USER, INPUT_NAME);

        //when
        boolean checkMatches = userRepository.matches(INVALID_ID, INVALID_PW);

        //then
        assertThat(checkMatches).isFalse();
    }

    @Test
    @DisplayName("UserRepository에 등록된 사용자 정보와 입력된 ID/PW와 일치하는 경우 true를 반환한다.")
    void matches_success() {
        //given
        userRepository.addUser(INPUT_ID, INPUT_PW, ROLE_USER, INPUT_NAME);

        //when
        boolean checkMatches = userRepository.matches(INPUT_ID, INPUT_PW);

        //then
        assertThat(checkMatches).isTrue();
    }

    @Test
    @DisplayName("UserRepository에 등록되지 않은 사용자를 불러올 경우 null을 반환한다.")
    void getUser_fail() {
        //given
        userRepository.addUser(INPUT_ID, INPUT_PW, ROLE_USER, INPUT_NAME);

        //when
        User user = userRepository.getUser(INVALID_ID);

        //then
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("UserRepository에 등록된 사용자를 불러올 경우 User 조회가 성공한다.")
    void getUser_success() {
        //given
        userRepository.addUser(INPUT_ID, INPUT_PW, ROLE_USER, INPUT_NAME);

        //when
        User getUser = userRepository.getUser(INPUT_ID);

        //then
        assertThat(getUser.getId()).isEqualTo(inputUser.getName());
        assertThat(getUser.getPassword()).isEqualTo(inputUser.getPassword());
        assertThat(getUser.getRole()).isEqualTo(inputUser.getRole());
        assertThat(getUser.getName()).isEqualTo(inputUser.getName());
    }

    @Test
    @DisplayName("이미 등록된 사용자를 등록할 경우 예외를 반환한다.")
    void addUser_fail_throwUserAlreadyExistsException() {
        //given
        userRepository.addUser(INPUT_ID, INPUT_PW, ROLE_USER, INPUT_NAME);

        //when, then
        assertThatThrownBy(() -> userRepository.addUser(INPUT_ID, INPUT_PW, ROLE_USER, INPUT_NAME))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContainingAll("Already existed user: ", INPUT_ID);
    }

    @Test
    @DisplayName("사용자 등록에 성공할 경우 User가 예외 없이 등록된다.")
    void addUser_success() {
        //when, then
        assertThatCode(() -> userRepository.addUser(INPUT_ID, INPUT_PW, ROLE_USER, INPUT_NAME))
                .doesNotThrowAnyException();
    }
}