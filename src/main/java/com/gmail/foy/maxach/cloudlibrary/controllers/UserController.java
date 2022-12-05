package com.gmail.foy.maxach.cloudlibrary.controllers;

import com.gmail.foy.maxach.cloudlibrary.dtos.PostDto;
import com.gmail.foy.maxach.cloudlibrary.dtos.UserDto;
import com.gmail.foy.maxach.cloudlibrary.exceptions.ForbiddenException;
import com.gmail.foy.maxach.cloudlibrary.models.Post;
import com.gmail.foy.maxach.cloudlibrary.models.User;
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
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private PostService postService;


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody User user) {
        return new UserDto(userService.createUser(user));
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Long id) {
        return new UserDto(userService.getUserById(id));
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers(
            @RequestParam(name = "sort", defaultValue = "old") String sortedType,
            @PageableDefault(sort = "old") Pageable pageable) {

        List<User> users;
        if(sortedType.equals("old")) {
            users = userService.getOldUsersByPageAndSize(pageable.getPageNumber(), pageable.getPageSize());
        }
        else if(sortedType.equals("new")) {
            users = userService.getNewUsersByPageAndSize(pageable.getPageNumber(), pageable.getPageSize());
        }
        else {
            users = Collections.emptyList();
        }

        return users.stream().map(user -> new UserDto(user)).collect(Collectors.toList());
    }


    @GetMapping("/{id}/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getAllPostsByUserId(@PathVariable Long id,
            @RequestParam(name = "sort", defaultValue = "old") String sortedType,
            @PageableDefault(sort = "old") Pageable pageable) {
        List<Post> posts;
        if(sortedType.equals("old")) {
            posts = postService.getOldPostsByPageAndSizeAndUserId(pageable.getPageNumber(), pageable.getPageSize(), id);
        }
        else if(sortedType.equals("new")) {
            posts = postService.getNewPostsByPageAndSizeAndUserId(pageable.getPageNumber(), pageable.getPageSize(), id);
        }
        else {
            posts = Collections.emptyList();
        }

        return posts.stream().map(post -> new PostDto(post)).collect(Collectors.toList());
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUserById(@PathVariable Long id, @Valid @RequestBody User user) {
        Long idFromHeaders = UserDetailsImp.getUserIdFromHeaders();
        String roleFromHeaders = UserDetailsImp.getUserRoleFromHeaders();

        if(!roleFromHeaders.equals("ROLE_ADMIN") && idFromHeaders != id) {
            throw new ForbiddenException("You can update only own account");
        }
        userService.updateUserById(user, id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        Long idFromHeaders = UserDetailsImp.getUserIdFromHeaders();
        String roleFromHeaders = UserDetailsImp.getUserRoleFromHeaders();

        if(!roleFromHeaders.equals("ROLE_ADMIN") && idFromHeaders != id) {
            throw new ForbiddenException("You can delete only own account");
        }

        User userForDeleting = userService.getUserById(id);
        if(userForDeleting.getRole().getName().equals("ROLE_ADMIN") && idFromHeaders != id) {
            throw new ForbiddenException("You can't delete another admin");
        }

        userService.deleteUserById(id);
    }
}
