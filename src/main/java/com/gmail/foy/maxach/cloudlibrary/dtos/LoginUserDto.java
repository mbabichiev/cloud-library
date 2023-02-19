package com.gmail.foy.maxach.cloudlibrary.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@ApiModel(value = "Login user")
public class LoginUserDto {

    @ApiModelProperty(example = "mbabichiev")
    @Size(min = 6, max = 30)
    private String login;

    @ApiModelProperty(example = "password")
    @Size(min = 6, max = 255)
    private String password;
}
