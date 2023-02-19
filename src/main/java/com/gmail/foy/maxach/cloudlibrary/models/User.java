package com.gmail.foy.maxach.cloudlibrary.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;


@Data
@ToString
@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "users")
@ApiModel(value = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Size(min = 6, max = 30)
    @ApiModelProperty(example = "mbabichiev")
    private String login;

    @Size(min = 6, max = 255)
    @ApiModelProperty(example = "password")
    private String password;

    @Email
    @ApiModelProperty(example = "example@gmail.com")
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @ApiModelProperty(hidden = true)
    private Role role;
}
