package com.gmail.foy.maxach.cloudlibrary.controllers;

import com.gmail.foy.maxach.cloudlibrary.dtos.CreatePostDto;
import com.gmail.foy.maxach.cloudlibrary.dtos.PostDto;
import com.gmail.foy.maxach.cloudlibrary.exceptions.ForbiddenException;
import com.gmail.foy.maxach.cloudlibrary.models.Post;
import com.gmail.foy.maxach.cloudlibrary.services.PostService;
import com.gmail.foy.maxach.cloudlibrary.services.UserService;
import com.gmail.foy.maxach.cloudlibrary.utils.UserDetailsImp;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/api/posts")
@Api(tags = "Posts")
public class PostController {

    private PostService postService;
    private UserService userService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create post", notes = "You should be authorized")
    @ApiResponses({
            @ApiResponse(code = 400, message = "You already have a post with the title 'Title'")
    })
    public PostDto createPost(@Valid @RequestBody CreatePostDto createPostDto) {
        Post post = new Post();
        post.setTitle(createPostDto.getTitle());
        post.setContent(createPostDto.getContent());
        post.setUser(userService.getUserById(UserDetailsImp.getUserIdFromHeaders()));
        return new PostDto(postService.createPost(post));
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get post by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Post with id: 1 doesn't found")
    })
    public PostDto getPostById(@PathVariable Long id) {
        return new PostDto(postService.getPostById(id));
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all posts by sorting type and page size")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort", dataType = "string", paramType = "query", defaultValue = "old"),
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", defaultValue = "10")
    })
    public List<PostDto> getAllPosts(
            @RequestParam(name = "sort", defaultValue = "old") String sortedType,
            @PageableDefault(sort = "old") Pageable pageable) {

        List<Post> posts;
        if(sortedType.equals("old")) {
            posts = postService.getOldPostsByPageAndSize(pageable.getPageNumber(), pageable.getPageSize());
        }
        else if(sortedType.equals("new")) {
            posts = postService.getNewPostsByPageAndSize(pageable.getPageNumber(), pageable.getPageSize());
        }
        else {
            posts = Collections.emptyList();
        }

        return posts.stream().map(post -> new PostDto(post)).collect(Collectors.toList());
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Update post by id", notes = "You should be authorized")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Post with id: 1 doesn't found")
    })
    public void updatePostById(@PathVariable Long id, @Valid @RequestBody CreatePostDto createPostDto) {
        Long idFromHeaders = UserDetailsImp.getUserIdFromHeaders();
        String roleFromHeaders = UserDetailsImp.getUserRoleFromHeaders();

        if(!roleFromHeaders.equals("ROLE_ADMIN") && idFromHeaders != id) {
            throw new ForbiddenException("You can update only own posts");
        }

        Post post = new Post();
        post.setTitle(post.getTitle());
        post.setContent(post.getContent());

        postService.updatePostById(id, post);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete post by id", notes = "You should be authorized")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Post with id: 1 doesn't found")
    })
    public void deletePostById(@PathVariable Long id) {
        Long idFromHeaders = UserDetailsImp.getUserIdFromHeaders();
        String roleFromHeaders = UserDetailsImp.getUserRoleFromHeaders();

        if(!roleFromHeaders.equals("ROLE_ADMIN") && idFromHeaders != id) {
            throw new ForbiddenException("You can delete only own posts");
        }

        postService.deletePostById(id);
    }
}
