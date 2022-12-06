package com.gmail.foy.maxach.cloudlibrary.services;

import com.gmail.foy.maxach.cloudlibrary.exceptions.PasswordIsInvalidException;;
import com.gmail.foy.maxach.cloudlibrary.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthServiceTest {

    private static AuthService authService;
    @MockBean
    private static UserService userService;
    @MockBean
    private static PasswordEncoder passwordEncoder;


    @BeforeAll
    static void setup() {
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authService = new AuthService(userService, passwordEncoder);
    }


    @Test
    void register_success() {
        User sourceUser = User.builder()
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();

        User expectedUser = User.builder()
                .id(1l)
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();

        Mockito.doReturn(expectedUser)
                .when(userService)
                .createUser(sourceUser);

        User actualUser = authService.register(sourceUser);

        Assertions.assertEquals(actualUser, expectedUser);
    }


    @Test
    void login_success() {
        String login = "login";
        String password = "password";
        User sourceUser = User.builder()
                .id(1l)
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();

        Mockito.doReturn(sourceUser)
                .when(userService)
                .getUserByLogin(login);

        Mockito.doReturn(true)
                .when(passwordEncoder)
                .matches(password, sourceUser.getPassword());

        User actualUser = authService.login(login, password);

        User expectedUser = User.builder()
                .id(1l)
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();

        Assertions.assertEquals(actualUser, expectedUser);
    }


    @Test
    void login_failure() {
        String login = "login";
        String password = "password";
        User sourceUser = User.builder()
                .id(1l)
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();

        Mockito.doReturn(sourceUser)
                .when(userService)
                .getUserByLogin(login);

        Mockito.doReturn(false)
                .when(passwordEncoder)
                .matches(password, sourceUser.getPassword());

        String expectedMessage = "Invalid password";

        String actualMessage = Assertions.assertThrows(PasswordIsInvalidException.class, () ->
                authService.login(login, password)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

}
