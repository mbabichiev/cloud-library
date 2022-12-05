package com.gmail.foy.maxach.cloudlibrary.services;

import com.gmail.foy.maxach.cloudlibrary.exceptions.PostAlreadyExistsException;
import com.gmail.foy.maxach.cloudlibrary.exceptions.PostNotFoundException;
import com.gmail.foy.maxach.cloudlibrary.models.Post;
import com.gmail.foy.maxach.cloudlibrary.repositories.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
public class PostService {

    private PostRepository postRepository;
    private final static String ERROR_YOU_ALREADY_HAVE_POST_WITH_TITLE = "You already have a post with the title '%s'";
    private final static String ERROR_POST_WITH_ID_DOES_NOT_FOUND = "Post with id: %d doesn't found";


    public Post createPost(Post post) {
        log.info("Create post with title '{}' by user with id: {}", post.getTitle(), post.getUser().getId());
        if(postRepository.existsByUserIdAndTitle(post.getUser().getId(), post.getTitle())) {
            throw new PostAlreadyExistsException(String.format(ERROR_YOU_ALREADY_HAVE_POST_WITH_TITLE, post.getTitle()));
        }
        post.setPublishDate(new Date().getTime());
        return postRepository.save(post);
    }


    public Post getPostById(Long id) {
        log.info("Get post with id: {}", id);
        if(!postRepository.existsById(id)) {
            throw new PostNotFoundException(String.format(ERROR_POST_WITH_ID_DOES_NOT_FOUND, id));
        }
        return postRepository.findById(id).get();
    }


    public List<Post> getOldPostsByPageAndSize(int page, int size) {
        log.info("Get old posts with page {} and size {}", page, size);
        Page<Post> posts = postRepository.findAll(PageRequest.of(page, size, Sort.by("publishDate").ascending()));
        return posts.stream().toList();
    }


    public List<Post> getNewPostsByPageAndSize(int page, int size) {
        log.info("Get new posts with page {} and size {}", page, size);
        Page<Post> posts = postRepository.findAll(PageRequest.of(page, size, Sort.by("publishDate").descending()));
        return posts.stream().toList();
    }


    public List<Post> getOldPostsByPageAndSizeAndUserId(int page, int size, Long userId) {
        log.info("Get old posts with page {} and size {} and userId {}", page, size, userId);
        Page<Post> posts = postRepository.findByUserId(userId, PageRequest.of(page, size, Sort.by("publishDate").ascending()));
        return posts.stream().toList();
    }


    public List<Post> getNewPostsByPageAndSizeAndUserId(int page, int size, Long userId) {
        log.info("Get new posts with page {} and size {} and userId {}", page, size, userId);
        Page<Post> posts = postRepository.findByUserId(userId, PageRequest.of(page, size, Sort.by("publishDate").descending()));
        return posts.stream().toList();
    }


    public void updatePostById(Long id, Post post) {
        log.info("Update post with id: {}", id);

        Post oldPost = getPostById(id);

        if(post.getTitle() != null && postRepository.findByTitle(post.getTitle()) != null &&
                postRepository.findByTitle(post.getTitle()).getId() != id) {
            throw new PostAlreadyExistsException(String.format(ERROR_YOU_ALREADY_HAVE_POST_WITH_TITLE, post.getTitle()));
        }

        postRepository.save(Post.builder()
                .id(id)
                .user(oldPost.getUser())
                .publishDate(oldPost.getPublishDate())
                .title(post.getTitle() == null ? oldPost.getTitle() : post.getTitle())
                .content(post.getContent() == null ? oldPost.getContent() : post.getContent())
                .build());
    }


    public void deletePostById(Long id) {
        log.info("Delete post with id: {}", id);
        if(!postRepository.existsById(id)) {
            log.error("Post with id {} does not found");
            throw new PostNotFoundException(String.format(ERROR_POST_WITH_ID_DOES_NOT_FOUND, id));
        }

        postRepository.deleteById(id);
    }

}
