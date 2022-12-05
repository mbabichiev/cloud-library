package com.gmail.foy.maxach.cloudlibrary.controllers;

import com.gmail.foy.maxach.cloudlibrary.dtos.PostDto;
import com.gmail.foy.maxach.cloudlibrary.exceptions.ForbiddenException;
import com.gmail.foy.maxach.cloudlibrary.models.Post;
import com.gmail.foy.maxach.cloudlibrary.services.PostService;
import com.gmail.foy.maxach.cloudlibrary.services.UserService;
import com.gmail.foy.maxach.cloudlibrary.utils.UserDetailsImp;
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
public class PostController {

    private PostService postService;
    private UserService userService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@Valid @RequestBody Post post) {
        post.setUser(userService.getUserById(UserDetailsImp.getUserIdFromHeaders()));
        return new PostDto(postService.createPost(post));
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto getPostById(@PathVariable Long id) {
        return new PostDto(postService.getPostById(id));
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
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
    public void updatePostById(@PathVariable Long id, @Valid @RequestBody Post post) {
        Long idFromHeaders = UserDetailsImp.getUserIdFromHeaders();
        String roleFromHeaders = UserDetailsImp.getUserRoleFromHeaders();

        if(!roleFromHeaders.equals("ROLE_ADMIN") && idFromHeaders != id) {
            throw new ForbiddenException("You can update only own posts");
        }

        postService.updatePostById(id, post);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostById(@PathVariable Long id) {
        Long idFromHeaders = UserDetailsImp.getUserIdFromHeaders();
        String roleFromHeaders = UserDetailsImp.getUserRoleFromHeaders();

        if(!roleFromHeaders.equals("ROLE_ADMIN") && idFromHeaders != id) {
            throw new ForbiddenException("You can delete only own posts");
        }

        postService.deletePostById(id);
    }
}
