package com.gmail.foy.maxach.cloudlibrary.controllers;

import com.gmail.foy.maxach.cloudlibrary.dtos.UserDto;
import com.gmail.foy.maxach.cloudlibrary.models.User;
import com.gmail.foy.maxach.cloudlibrary.services.AuthService;
import com.gmail.foy.maxach.cloudlibrary.services.TokenService;
import com.gmail.foy.maxach.cloudlibrary.services.UserService;
import com.gmail.foy.maxach.cloudlibrary.utils.UserDetailsImp;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;
    private UserService userService;
    private TokenService tokenService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@Valid @RequestBody User user) {
        User registeredUser = authService.register(user);

        UserDto userDto = new UserDto(registeredUser);
        userDto.setToken(tokenService.generateToken(user.getId()));

        return userDto;
    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserDto login(@Valid @RequestBody User user) {
        User signedInUser = authService.login(user.getLogin(), user.getPassword());

        UserDto userDto = new UserDto(signedInUser);
        userDto.setToken(tokenService.generateToken(signedInUser.getId()));

        return userDto;
    }


    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getOwnProfile() {
        Long idFromHeaders = UserDetailsImp.getUserIdFromHeaders();
        return new UserDto(userService.getUserById(idFromHeaders));
    }






}
