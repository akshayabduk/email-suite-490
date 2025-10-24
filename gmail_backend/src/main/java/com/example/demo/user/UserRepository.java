package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * PUBLIC_INTERFACE
 * Repository for User persistence operations.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // PUBLIC_INTERFACE
    /** Finds user by email if exists. */
    Optional<User> findByEmail(String email);

    // PUBLIC_INTERFACE
    /** Checks whether a user exists with given email. */
    boolean existsByEmail(String email);
}
