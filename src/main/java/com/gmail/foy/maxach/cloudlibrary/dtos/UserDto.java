package com.gmail.foy.maxach.cloudlibrary.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.foy.maxach.cloudlibrary.models.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Getting user")
public class UserDto {

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


    public UserDto(User user) {
        id = user.getId();
        login = user.getLogin();
        email = user.getEmail();
        role = user.getRole().getName();
    }
}
