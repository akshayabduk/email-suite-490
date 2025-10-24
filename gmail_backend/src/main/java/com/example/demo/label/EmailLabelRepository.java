package com.example.demo.label;

import com.example.demo.email.Email;
import com.example.demo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * PUBLIC_INTERFACE
 * Repository for EmailLabel assignment between emails and labels.
 */
public interface EmailLabelRepository extends JpaRepository<EmailLabel, Long> {

    // PUBLIC_INTERFACE
    Optional<EmailLabel> findByOwnerAndEmailAndLabel(User owner, Email email, Label label);

    // PUBLIC_INTERFACE
    List<EmailLabel> findByOwnerAndEmail(User owner, Email email);

    // PUBLIC_INTERFACE
    /** Page email ids for owner by label. */
    @Query("""
            select el.email.id from EmailLabel el
            where el.owner = :owner and el.label = :label
            order by el.createdAt desc
            """)
    Page<Long> findEmailIdsByOwnerAndLabel(@Param("owner") User owner,
                                           @Param("label") Label label,
                                           Pageable pageable);
}
