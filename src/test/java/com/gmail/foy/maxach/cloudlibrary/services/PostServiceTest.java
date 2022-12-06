package com.gmail.foy.maxach.cloudlibrary.services;

import com.gmail.foy.maxach.cloudlibrary.exceptions.PostAlreadyExistsException;
import com.gmail.foy.maxach.cloudlibrary.exceptions.PostNotFoundException;
import com.gmail.foy.maxach.cloudlibrary.models.Post;
import com.gmail.foy.maxach.cloudlibrary.models.Role;
import com.gmail.foy.maxach.cloudlibrary.models.User;
import com.gmail.foy.maxach.cloudlibrary.repositories.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

public class PostServiceTest {

    private static PostService postService;
    @MockBean
    private static PostRepository postRepository;


    @BeforeAll
    static void setup() {
        postRepository = Mockito.mock(PostRepository.class);
        postService = new PostService(postRepository);
    }


    @Test
    void createPost_success() {
        User sourceUser = User.builder()
                .id(1l)
                .login("login")
                .password("password")
                .email("example@gmail.com")
                .role(
                        Role.builder()
                        .id(1)
                        .name("ROLE_USER")
                        .build())
                .build();

        Post sourcePost = Post.builder()
                .title("title")
                .content("content")
                .user(sourceUser)
                .build();

        Post expectedPost = Post.builder()
                .id(1l)
                .title("title")
                .content("content")
                .user(sourceUser)
                .publishDate(1l)
                .build();

        Mockito.doReturn(false)
                .when(postRepository)
                .existsByUserIdAndTitle(Mockito.eq(sourcePost.getUser().getId()), Mockito.eq(sourcePost.getTitle()));

        Mockito.doReturn(expectedPost)
                .when(postRepository)
                .save(Mockito.eq(sourcePost));

        Post actualPost = postService.createPost(sourcePost);

        Mockito.verify(postRepository, Mockito.times(1))
                .save(Mockito.eq(sourcePost));

        Assertions.assertEquals(actualPost, expectedPost);
    }


    @Test
    void createPost_failure() {
        Long sourceUserId = 1l;
        String sourceTitle = "title";
        Post sourcePost = Post.builder()
                .title("title")
                .content("content")
                .user(User.builder()
                        .id(1l)
                        .login("login")
                        .password("password")
                        .email("example@gmail.com")
                        .role(
                                Role.builder()
                                        .id(1)
                                        .name("ROLE_USER")
                                        .build())
                        .build())
                .build();

        Mockito.doReturn(true)
                .when(postRepository)
                .existsByUserIdAndTitle(Mockito.eq(sourceUserId), Mockito.eq(sourceTitle));

        String expectedMessage = "You already have a post with the title 'title'";

        String actualMessage = Assertions.assertThrows(PostAlreadyExistsException.class, () ->
                postService.createPost(sourcePost)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void getPostById_success() {
        Long id = 1l;
        User sourceUser = User.builder()
                .id(1l)
                .login("login")
                .password("password")
                .email("example@gmail.com")
                .role(
                        Role.builder()
                                .id(1)
                                .name("ROLE_USER")
                                .build())
                .build();

        Post sourcePost = Post.builder()
                .id(id)
                .title("title")
                .content("content")
                .publishDate(1l)
                .user(sourceUser)
                .build();

        Mockito.doReturn(true)
                .when(postRepository)
                .existsById(id);

        Mockito.doReturn(Optional.of(sourcePost))
                .when(postRepository)
                .findById(id);

        Post actualPost = postService.getPostById(id);

        Post expectedPost = Post.builder()
                .id(id)
                .title("title")
                .content("content")
                .publishDate(1l)
                .user(sourceUser)
                .build();

        Assertions.assertEquals(actualPost, expectedPost);
    }


    @Test
    void getPostById_failure() {
        Long id = 1l;

        Mockito.doReturn(false)
                .when(postRepository)
                .existsById(id);

        String expectedMessage = "Post with id: 1 doesn't found";

        String actualMessage = Assertions.assertThrows(PostNotFoundException.class, () ->
                postService.getPostById(id)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void updatePostById_success() {
        Long id = 1l;
        Post newPostForUpdate = Post.builder()
                .id(id)
                .content("new content")
                .build();

        User sourceUser = User.builder()
                .id(1l)
                .login("login")
                .password("password")
                .email("example@gmail.com")
                .role(
                        Role.builder()
                                .id(1)
                                .name("ROLE_USER")
                                .build())
                .build();

        Post oldPost = Post.builder()
                .id(id)
                .title("title")
                .content("content")
                .publishDate(1l)
                .user(sourceUser)
                .build();

        Mockito.doReturn(true)
                .when(postRepository)
                .existsById(id);

        Mockito.doReturn(Optional.of(oldPost))
                .when(postRepository)
                .findById(id);

        postService.updatePostById(id, newPostForUpdate);

        Post expectedPost = Post.builder()
                .id(id)
                .title("title")
                .content("new content")
                .publishDate(1l)
                .user(sourceUser)
                .build();

        Mockito.verify(postRepository, Mockito.times(1))
                .save(Mockito.eq(expectedPost));
    }


    @Test
    void updatePostById_failure() {
        Long id = 1l;
        User sourceUser = User.builder()
                .id(1l)
                .login("login")
                .password("password")
                .email("example@gmail.com")
                .role(
                        Role.builder()
                                .id(1)
                                .name("ROLE_USER")
                                .build())
                .build();

        Post newPostForUpdate = Post.builder()
                .id(id)
                .title("title")
                .user(sourceUser)
                .build();

        Post oldPost = Post.builder()
                .id(id)
                .title("title")
                .content("content")
                .publishDate(1l)
                .user(sourceUser)
                .build();

        Post anotherPostWithSameTitle = Post.builder()
                .id(2l)
                .title("title")
                .content("content")
                .publishDate(1l)
                .user(sourceUser)
                .build();

        Mockito.doReturn(true)
                .when(postRepository)
                .existsById(id);

        Mockito.doReturn(Optional.of(oldPost))
                .when(postRepository)
                .findById(id);

        Mockito.doReturn(anotherPostWithSameTitle)
                .when(postRepository)
                .findByTitle(newPostForUpdate.getTitle());

        String expectedMessage = "You already have a post with the title 'title'";

        String actualMessage = Assertions.assertThrows(PostAlreadyExistsException.class, () ->
                postService.updatePostById(id, newPostForUpdate)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void deletePostById_success() {
        Long id = 1l;

        Mockito.doReturn(true)
                .when(postRepository)
                .existsById(id);

        postService.deletePostById(id);

        Mockito.verify(postRepository, Mockito.times(1)).deleteById(Mockito.eq(id));
    }


    @Test
    void deletePostById_failure() {
        Long id = 1l;

        Mockito.doReturn(false)
                .when(postRepository)
                .existsById(Mockito.eq(id));

        String expectedMessage = "Post with id: 1 doesn't found";

        String actualMessage = Assertions.assertThrows(PostNotFoundException.class, () ->
                postService.deletePostById(id)).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

}
