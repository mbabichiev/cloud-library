package com.gmail.foy.maxach.cloudlibrary.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.foy.maxach.cloudlibrary.models.Post;
import lombok.Data;

@Data
public class PostDto {

    @JsonProperty("Id")
    private Long id;
    @JsonProperty("User id")
    private Long user_id;
    @JsonProperty("User login")
    private String user_login;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Content")
    private String content;
    @JsonProperty("Publish date")
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
