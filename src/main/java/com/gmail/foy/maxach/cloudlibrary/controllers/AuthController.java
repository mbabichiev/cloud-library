package com.gmail.foy.maxach.cloudlibrary.controllers;

import com.gmail.foy.maxach.cloudlibrary.dtos.AuthUserDto;
import com.gmail.foy.maxach.cloudlibrary.dtos.LoginUserDto;
import com.gmail.foy.maxach.cloudlibrary.dtos.UserDto;
import com.gmail.foy.maxach.cloudlibrary.models.User;
import com.gmail.foy.maxach.cloudlibrary.services.AuthService;
import com.gmail.foy.maxach.cloudlibrary.services.TokenService;
import com.gmail.foy.maxach.cloudlibrary.services.UserService;
import com.gmail.foy.maxach.cloudlibrary.utils.UserDetailsImp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Api(tags = "Authorization")
public class AuthController {

    private AuthService authService;
    private UserService userService;
    private TokenService tokenService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Register your profile", notes = "You should be unauthorized")
    @ApiResponses({
            @ApiResponse(code = 400, message =
                    "- User with login 'mbabichiev' already exists\n" +
                    "- Email 'example@gmail.com' already in use")
    })
    public AuthUserDto register(@Valid @RequestBody User user) {
        User registeredUser = authService.register(user);

        AuthUserDto userDto = new AuthUserDto(registeredUser);
        userDto.setToken(tokenService.generateToken(user.getId()));

        return userDto;
    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Login", notes = "You should be unauthorized")
    @ApiResponses({
            @ApiResponse(code = 400, message =
                    "- User with login 'mbabichiev' not found\n" +
                    "- Invalid password")
    })
    public AuthUserDto login(@Valid @RequestBody LoginUserDto user) {
        User signedInUser = authService.login(user.getLogin(), user.getPassword());

        AuthUserDto userDto = new AuthUserDto(signedInUser);
        userDto.setToken(tokenService.generateToken(signedInUser.getId()));

        return userDto;
    }


    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get information about own profile", notes = "You should be authorized")
    public UserDto getOwnProfile() {
        Long idFromHeaders = UserDetailsImp.getUserIdFromHeaders();
        return new UserDto(userService.getUserById(idFromHeaders));
    }






}
