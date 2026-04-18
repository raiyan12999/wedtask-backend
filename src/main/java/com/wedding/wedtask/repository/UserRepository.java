package com.wedding.wedtask.repository;


import com.wedding.wedtask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByNameIgnoreCase(String name);
    List<User> findByStatus(User.UserStatus status);
    boolean existsByNameIgnoreCase(String name);
}
