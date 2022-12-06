package com.gmail.foy.maxach.cloudlibrary.services;

import com.gmail.foy.maxach.cloudlibrary.exceptions.UserAlreadyExistsException;
import com.gmail.foy.maxach.cloudlibrary.exceptions.UserNotFoundException;
import com.gmail.foy.maxach.cloudlibrary.models.Role;
import com.gmail.foy.maxach.cloudlibrary.models.User;
import com.gmail.foy.maxach.cloudlibrary.repositories.RoleRepository;
import com.gmail.foy.maxach.cloudlibrary.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserServiceTest {

    private static UserService userService;
    @MockBean
    private static UserRepository userRepository;
    @MockBean
    private static RoleRepository roleRepository;
    @MockBean
    private static PasswordEncoder passwordEncoder;


    @BeforeAll
    static void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        roleRepository = Mockito.mock(RoleRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserService();
        userService.setUserRepository(userRepository);
        userService.setRoleRepository(roleRepository);
        userService.setPasswordEncoder(passwordEncoder);
    }


    @Test
    void createUser_success() {
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

        Role sourceRole = Role.builder()
                .id(1)
                .name("ROLE_USER")
                .build();

        Mockito.doReturn(false)
                .when(userRepository)
                .existsByLogin(Mockito.eq(sourceUser.getLogin()));

        Mockito.doReturn(false)
                .when(userRepository)
                .existsByEmail(Mockito.eq(sourceUser.getEmail()));

        Mockito.doReturn(true)
                .when(roleRepository)
                .existsByName(Mockito.eq("ROLE_USER"));

        Mockito.doReturn(sourceRole)
                .when(roleRepository)
                .findByName(Mockito.eq("ROLE_USER"));

        Mockito.doReturn("password")
                .when(passwordEncoder)
                .encode(Mockito.eq(sourceUser.getPassword()));

        Mockito.doReturn(expectedUser)
                .when(userRepository)
                .save(Mockito.eq(sourceUser));

        User actualUser = userService.createUser(sourceUser);

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.eq(sourceUser));

        Assertions.assertEquals(actualUser, expectedUser);
    }


    @Test
    void createUser_failureLoginAlreadyExists() {
        User sourceUser = User.builder()
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();

        Mockito.doReturn(true)
                .when(userRepository)
                .existsByLogin(Mockito.eq(sourceUser.getLogin()));

        String expectedMessage = "User with login 'login' already exists";

        String actualMessage = Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                userService.createUser(sourceUser)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void createUser_failureEmailAlreadyInUse() {
        User sourceUser = User.builder()
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();

        Mockito.doReturn(false)
                .when(userRepository)
                .existsByLogin(Mockito.eq(sourceUser.getLogin()));

        Mockito.doReturn(true)
                .when(userRepository)
                .existsByEmail(Mockito.eq(sourceUser.getEmail()));

        String expectedMessage = "Email 'email@gmail.com' already in use";

        String actualMessage = Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                userService.createUser(sourceUser)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void getUserById_success() {
        Long id = 1l;

        User sourceUser = User.builder()
                .id(id)
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();

        Mockito.doReturn(true)
                .when(userRepository)
                .existsById(Mockito.eq(id));

        Mockito.doReturn(Optional.of(sourceUser))
                .when(userRepository)
                .findById(Mockito.eq(id));

        User actualUser = userService.getUserById(id);

        User expectedUser = User.builder()
                .id(id)
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();

        Assertions.assertEquals(actualUser, expectedUser);
    }


    @Test
    void getUserById_failure() {
        Long id = 1l;

        Mockito.doReturn(false)
                .when(userRepository)
                .existsById(Mockito.eq(id));

        String expectedMessage = "User with id: 1 doesn't found";

        String actualMessage = Assertions.assertThrows(UserNotFoundException.class, () ->
                userService.getUserById(id)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void getUserByLogin_success() {
        String sourceLogin = "login";

        User sourceUser = User.builder()
                .id(1l)
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();

        Mockito.doReturn(true)
                .when(userRepository)
                .existsByLogin(Mockito.eq(sourceLogin));

        Mockito.doReturn(sourceUser)
                .when(userRepository)
                .findByLogin(Mockito.eq(sourceLogin));

        User actualUser = userService.getUserByLogin(sourceLogin);

        User expectedUser = User.builder()
                .id(1l)
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();

        Assertions.assertEquals(actualUser, expectedUser);
    }


    @Test
    void getUserByLogin_failure() {
        String sourceLogin = "login";

        Mockito.doReturn(false)
                .when(userRepository)
                .existsByLogin(Mockito.eq(sourceLogin));

        String expectedMessage = "User with login 'login' not found";

        String actualMessage = Assertions.assertThrows(UserNotFoundException.class, () ->
                userService.getUserByLogin(sourceLogin)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void updateUserById_success() {
        Long id = 1l;
        User sourceUserForUpdate = User.builder()
                .email("emailnew@gmail.com")
                .build();

        User sourceUser = User.builder()
                .id(id)
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();

        Mockito.doReturn(true)
                .when(userRepository)
                .existsById(Mockito.eq(id));

        Mockito.doReturn(Optional.of(sourceUser))
                .when(userRepository)
                .findById(Mockito.eq(id));

        userService.updateUserById(sourceUserForUpdate, id);

        User expectedUser = User.builder()
                .id(1l)
                .login("login")
                .password("password")
                .email("emailnew@gmail.com")
                .build();

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.eq(expectedUser));
    }


    @Test
    void updateUserById_failure() {
        Long id = 1l;

        User newUser = User.builder()
                .id(id)
                .login("newlogin")
                .build();

        User oldUser = User.builder()
                .id(id)
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();

        User anotherUserWithSameLogin = User.builder()
                .id(2l)
                .login("newlogin")
                .password("password")
                .email("anemail@gmail.com")
                .build();

        Mockito.doReturn(true)
                .when(userRepository)
                .existsById(Mockito.eq(id));

        Mockito.doReturn(Optional.of(oldUser))
                .when(userRepository)
                .findById(Mockito.eq(id));

        Mockito.doReturn(true)
                .when(userRepository)
                .existsByLogin("newlogin");

        Mockito.doReturn(anotherUserWithSameLogin)
                .when(userRepository)
                .findByLogin("newlogin");

        String expectedMessage = "User with login 'newlogin' already exists";

        String actualMessage = Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                userService.updateUserById(newUser, id)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void deleteUserById_success() {
        Long id = 1l;

        Mockito.doReturn(true)
                .when(userRepository)
                .existsById(Mockito.eq(id));

        userService.deleteUserById(id);

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(Mockito.eq(id));
    }


    @Test
    void deleteUserById_failure() {
        Long id = 1l;

        Mockito.doReturn(false)
                .when(userRepository)
                .existsById(Mockito.eq(id));

        String expectedMessage = "User with id: 1 doesn't found";

        String actualMessage = Assertions.assertThrows(UserNotFoundException.class, () ->
                userService.deleteUserById(id)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

}
