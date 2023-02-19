package com.gmail.foy.maxach.cloudlibrary.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@ToString
@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "posts")
@ApiModel(value = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @ApiModelProperty(hidden = false)
    private Long id;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ApiModelProperty(hidden = false)
    private User user;

    @Size(min = 6, max = 30)
    @ApiModelProperty(example = "Some title")
    private String title;

    @Size(min = 6)
    @ApiModelProperty(example = "Some content")
    private String content;

    @Column(name = "publish_date")
    @ApiModelProperty(hidden = true)
    private Long publishDate;
}
