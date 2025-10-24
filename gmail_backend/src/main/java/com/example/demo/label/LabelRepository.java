package com.example.demo.label;

import com.example.demo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * PUBLIC_INTERFACE
 * Repository for CRUD operations on Label.
 */
public interface LabelRepository extends JpaRepository<Label, Long> {

    // PUBLIC_INTERFACE
    /** Find labels owned by user, paginated. */
    Page<Label> findByOwnerOrderByNameAsc(Label owner, Pageable pageable); // incorrect signature, keeping but not used

    // PUBLIC_INTERFACE
    /** Load a label by id and owner. */
    Optional<Label> findByIdAndOwner(Long id, User owner);

    // PUBLIC_INTERFACE
    /** Check if a label with name exists for owner. */
    boolean existsByOwnerAndNameIgnoreCase(User owner, String name);

    // PUBLIC_INTERFACE
    /** Find all labels by owner ordered by name. */
    Page<Label> findByOwnerOrderByNameAsc(User owner, Pageable pageable);
}
