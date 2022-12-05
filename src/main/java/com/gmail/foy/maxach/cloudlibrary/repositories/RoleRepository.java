package com.gmail.foy.maxach.cloudlibrary.repositories;

import com.gmail.foy.maxach.cloudlibrary.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
    boolean existsByName(String name);
}
