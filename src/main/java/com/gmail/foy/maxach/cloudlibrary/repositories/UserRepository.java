package com.gmail.foy.maxach.cloudlibrary.repositories;

import com.gmail.foy.maxach.cloudlibrary.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    User findByLogin(String login);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);
}
