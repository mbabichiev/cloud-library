package com.gmail.foy.maxach.cloudlibrary.models;

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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 6, max = 30)
    private String login;
    @Size(min = 6, max = 30)
    private String password;
    @Email
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
