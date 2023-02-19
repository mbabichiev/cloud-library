package com.gmail.foy.maxach.cloudlibrary.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@ApiModel(value = "Creating post")
public class CreatePostDto {

    @Size(min = 6, max = 30)
    @ApiModelProperty(example = "Title")
    private String title;

    @Size(min = 6)
    @ApiModelProperty(example = "Some content")
    private String content;
}
