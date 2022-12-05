package com.gmail.foy.maxach.cloudlibrary.repositories;

import com.gmail.foy.maxach.cloudlibrary.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository <Post, Long> {
    boolean existsByUserIdAndTitle(Long id, String title);
    Post findByTitle(String title);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByUserId(Long id, Pageable pageable);
}
