package com.gmail.foy.maxach.cloudlibrary.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.foy.maxach.cloudlibrary.models.User;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @JsonProperty("Id")
    private Long id;
    @JsonProperty("Login")
    private String login;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Role")
    private String role;
    @JsonProperty("Token")
    private String token;


    public UserDto(User user) {
        id = user.getId();
        login = user.getLogin();
        email = user.getEmail();
        role = user.getRole().getName();
    }
}
