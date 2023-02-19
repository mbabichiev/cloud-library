package com.gmail.foy.maxach.cloudlibrary.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.foy.maxach.cloudlibrary.models.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "Auth User")
public class AuthUserDto {

    @JsonProperty("Id")
    @ApiModelProperty(example = "1")
    private Long id;

    @JsonProperty("Login")
    @ApiModelProperty(example = "mbabichiev")
    private String login;

    @JsonProperty("Email")
    @ApiModelProperty(example = "example@gmail.com")
    private String email;

    @JsonProperty("Role")
    @ApiModelProperty(example = "USER_ROLE")
    private String role;

    @JsonProperty("Token")
    @ApiModelProperty(example = "JWT token")
    private String token;


    public AuthUserDto(User user) {
        id = user.getId();
        login = user.getLogin();
        email = user.getEmail();
        role = user.getRole().getName();
    }
}
