package com.gmail.foy.maxach.cloudlibrary.services;

import com.gmail.foy.maxach.cloudlibrary.exceptions.PasswordIsInvalidException;
import com.gmail.foy.maxach.cloudlibrary.models.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    private UserService userService;
    private PasswordEncoder passwordEncoder;


    public User register(User user) {
        return userService.createUser(user);
    }


    public User login(String login, String password) {
        User user = userService.getUserByLogin(login);
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordIsInvalidException("Invalid password");
        }
        return user;
    }
}
