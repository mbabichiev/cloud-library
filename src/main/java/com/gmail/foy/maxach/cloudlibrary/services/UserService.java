package com.gmail.foy.maxach.cloudlibrary.services;

import com.gmail.foy.maxach.cloudlibrary.exceptions.RoleNotFoundException;
import com.gmail.foy.maxach.cloudlibrary.exceptions.UserAlreadyExistsException;
import com.gmail.foy.maxach.cloudlibrary.exceptions.UserNotFoundException;
import com.gmail.foy.maxach.cloudlibrary.models.Role;
import com.gmail.foy.maxach.cloudlibrary.models.User;
import com.gmail.foy.maxach.cloudlibrary.repositories.RoleRepository;
import com.gmail.foy.maxach.cloudlibrary.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private final static String ERROR_USER_WITH_ID_DOES_NOT_FOUND = "User with id: %d doesn't found";
    private final static String ERROR_USER_WITH_LOGIN_ALREADY_EXISTS = "User with login '%s' already exists";
    private final static String ERROR_EMAIL_IS_ALREADY_IN_USE = "Email '%s' already in use";


    private Role getRoleByName(String name) {
        if(!roleRepository.existsByName(name)) {
            throw new RoleNotFoundException(String.format("Role with name '%s' not found", name));
        }
        return roleRepository.findByName(name);
    }


    private Role getDefaultRole() {
        return getRoleByName("ROLE_USER");
    }


    public User createUser(User user) {
        log.info("Create user with login: '{}'", user.getLogin());

        if(userRepository.existsByLogin(user.getLogin())) {
            throw new UserAlreadyExistsException(String.format(ERROR_USER_WITH_LOGIN_ALREADY_EXISTS, user.getLogin()));
        }
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(String.format(ERROR_EMAIL_IS_ALREADY_IN_USE, user.getEmail()));
        }

        Role role = user.getRole() == null ? getDefaultRole() : getRoleByName(user.getRole().getName());

        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);

        return registeredUser;
    }


    public User getUserById(Long id) {
        log.info("Get user with id {}", id);

        if(!userRepository.existsById(id)) {
            throw new UserNotFoundException(String.format(ERROR_USER_WITH_ID_DOES_NOT_FOUND, id));
        }
        return userRepository.findById(id).get();
    }


    public User getUserByLogin(String login) {
        log.info("Get user with login '{}'", login);

        if(!userRepository.existsByLogin(login)) {
            throw new UserNotFoundException("User with login '" + login + "' not found");
        }
        return userRepository.findByLogin(login);
    }


    public List<User> getOldUsersByPageAndSize(int page, int size) {
        log.info("Get old users with page {} and size {}", page, size);
        Page<User> users = userRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
        return users.stream().toList();
    }


    public List<User> getNewUsersByPageAndSize(int page, int size) {
        log.info("Get new users with page {} and size {}", page, size);
        Page<User> users = userRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
        return users.stream().toList();
    }


    public void updateUserById(User user, Long id) {
        log.info("Update user with id {}", id);

        User oldUser = getUserById(id);

        if(user.getLogin() != null
                && userRepository.existsByLogin(user.getLogin())
                && userRepository.findByLogin(user.getLogin()).getId() != id) {
            throw new UserAlreadyExistsException(String.format(ERROR_USER_WITH_LOGIN_ALREADY_EXISTS, user.getLogin()));
        }

        userRepository.save(User.builder()
                .id(id)
                .login(user.getLogin() == null ? oldUser.getLogin() : user.getLogin())
                .password(user.getPassword() == null ? oldUser.getPassword() : user.getPassword())
                .email(user.getEmail() == null ? oldUser.getEmail() : user.getEmail())
                .role(user.getRole() == null ? oldUser.getRole() : getRoleByName(user.getRole().getName()))
                .build()
        );
    }


    public void deleteUserById(Long id) {
        log.info("Delete user with id {}", id);

        if (!userRepository.existsById(id)){
            throw new UserNotFoundException(String.format(ERROR_USER_WITH_ID_DOES_NOT_FOUND, id));
        }
        userRepository.deleteById(id);
    }
}
