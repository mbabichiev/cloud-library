package com.gmail.foy.maxach.cloudlibrary.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.foy.maxach.cloudlibrary.models.Post;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Getting post")
public class PostDto {

    @JsonProperty("Id")
    @ApiModelProperty(example = "1")
    private Long id;

    @JsonProperty("User id")
    @ApiModelProperty(example = "1")
    private Long user_id;

    @JsonProperty("User login")
    @ApiModelProperty(example = "mbabichiev")
    private String user_login;

    @JsonProperty("Title")
    @ApiModelProperty(example = "Title")
    private String title;

    @JsonProperty("Content")
    @ApiModelProperty(example = "Some content")
    private String content;

    @JsonProperty("Publish date")
    @ApiModelProperty(example = "1673440295428")
    private Long publish_date;


    public PostDto(Post post) {
        id = post.getId();
        user_id = post.getUser().getId();
        user_login = post.getUser().getLogin();
        title = post.getTitle();
        content = post.getContent();
        publish_date = post.getPublishDate();
    }

}
