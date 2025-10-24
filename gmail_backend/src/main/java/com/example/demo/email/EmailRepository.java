package com.example.demo.email;

import com.example.demo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * PUBLIC_INTERFACE
 * Repository for Email queries across mailbox folders and search.
 */
public interface EmailRepository extends JpaRepository<Email, Long> {

    // Inbox: not deleted, not archived, folder=INBOX
    // PUBLIC_INTERFACE
    Page<Email> findByOwnerAndFolderAndIsDeletedFalseAndIsArchivedFalseOrderByReceivedAtDescCreatedAtDesc(User owner, Email.Folder folder, Pageable pageable);

    // Sent: not deleted, folder=SENT
    // PUBLIC_INTERFACE
    Page<Email> findByOwnerAndFolderAndIsDeletedFalseOrderBySentAtDescCreatedAtDesc(User owner, Email.Folder folder, Pageable pageable);

    // Drafts: folder=DRAFTS and not deleted
    // PUBLIC_INTERFACE
    Page<Email> findByOwnerAndFolderAndIsDeletedFalseOrderByUpdatedAtDescCreatedAtDesc(User owner, Email.Folder folder, Pageable pageable);

    // Trash: isDeleted true
    // PUBLIC_INTERFACE
    Page<Email> findByOwnerAndIsDeletedTrueOrderByUpdatedAtDescCreatedAtDesc(User owner, Pageable pageable);

    // Archived: isArchived true or folder=ARCHIVED
    // PUBLIC_INTERFACE
    Page<Email> findByOwnerAndIsArchivedTrueOrderByUpdatedAtDescCreatedAtDesc(User owner, Pageable pageable);

    // PUBLIC_INTERFACE
    /** Fulltext-like search across subject and bodies for the owner's mailbox (excluding deleted). */
    @Query("""
            select e from Email e
            where e.owner = :owner
              and e.isDeleted = false
              and (
                    lower(coalesce(e.subject,'')) like lower(concat('%', :q, '%'))
                 or lower(coalesce(e.bodyText,'')) like lower(concat('%', :q, '%'))
                 or lower(coalesce(e.bodyHtml,'')) like lower(concat('%', :q, '%'))
                 or lower(coalesce(e.fromAddr,'')) like lower(concat('%', :q, '%'))
                 or lower(coalesce(e.toAddrs,'')) like lower(concat('%', :q, '%'))
              )
            order by e.updatedAt desc, e.createdAt desc
            """)
    Page<Email> search(User owner, String q, Pageable pageable);
}
