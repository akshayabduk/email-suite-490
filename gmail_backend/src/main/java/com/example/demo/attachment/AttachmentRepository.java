package com.example.demo.attachment;

import com.example.demo.email.Email;
import com.example.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * PUBLIC_INTERFACE
 * Repository for Attachment persistence operations.
 */
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    // PUBLIC_INTERFACE
    Optional<Attachment> findByIdAndOwner(Long id, User owner);

    // PUBLIC_INTERFACE
    List<Attachment> findByOwnerAndEmailOrderByCreatedAtAsc(User owner, Email email);
}
