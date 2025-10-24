package com.example.demo.email;

import com.example.demo.email.dto.ComposeEmailRequest;
import com.example.demo.email.dto.EmailDetail;
import com.example.demo.email.dto.EmailListItem;
import com.example.demo.email.dto.UpdateFlagsRequest;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * PUBLIC_INTERFACE
 * Service encapsulating email mailbox operations, composing and internal delivery.
 */
@Service
public class EmailService {

    private final EmailRepository emails;
    private final UserRepository users;

    public EmailService(EmailRepository emails, UserRepository users) {
        this.emails = emails;
        this.users = users;
    }

    // PUBLIC_INTERFACE
    /** Returns paginated inbox for the owner. */
    @Transactional(readOnly = true)
    public Page<EmailListItem> listInbox(User owner, Pageable pageable) {
        return emails.findByOwnerAndFolderAndIsDeletedFalseAndIsArchivedFalseOrderByReceivedAtDescCreatedAtDesc(owner, Email.Folder.INBOX, pageable)
                .map(this::toListItem);
    }

    // PUBLIC_INTERFACE
    /** Returns paginated sent folder for the owner. */
    @Transactional(readOnly = true)
    public Page<EmailListItem> listSent(User owner, Pageable pageable) {
        return emails.findByOwnerAndFolderAndIsDeletedFalseOrderBySentAtDescCreatedAtDesc(owner, Email.Folder.SENT, pageable)
                .map(this::toListItem);
    }

    // PUBLIC_INTERFACE
    /** Returns paginated drafts for the owner. */
    @Transactional(readOnly = true)
    public Page<EmailListItem> listDrafts(User owner, Pageable pageable) {
        return emails.findByOwnerAndFolderAndIsDeletedFalseOrderByUpdatedAtDescCreatedAtDesc(owner, Email.Folder.DRAFTS, pageable)
                .map(this::toListItem);
    }

    // PUBLIC_INTERFACE
    /** Returns paginated trash for the owner. */
    @Transactional(readOnly = true)
    public Page<EmailListItem> listTrash(User owner, Pageable pageable) {
        return emails.findByOwnerAndIsDeletedTrueOrderByUpdatedAtDescCreatedAtDesc(owner, pageable)
                .map(this::toListItem);
    }

    // PUBLIC_INTERFACE
    /** Returns paginated archived for the owner. */
    @Transactional(readOnly = true)
    public Page<EmailListItem> listArchived(User owner, Pageable pageable) {
        return emails.findByOwnerAndIsArchivedTrueOrderByUpdatedAtDescCreatedAtDesc(owner, pageable)
                .map(this::toListItem);
    }

    // PUBLIC_INTERFACE
    /** Searches across subject and body fields for the owner. */
    @Transactional(readOnly = true)
    public Page<EmailListItem> search(User owner, String q, Pageable pageable) {
        return emails.search(owner, q == null ? "" : q.trim(), pageable).map(this::toListItem);
    }

    // PUBLIC_INTERFACE
    /** Fetch single email detail for the owner. */
    @Transactional(readOnly = true)
    public Optional<EmailDetail> getEmail(User owner, Long id) {
        return emails.findById(id)
                .filter(e -> e.getOwner().getId().equals(owner.getId()))
                .map(this::toDetail);
    }

    // PUBLIC_INTERFACE
    /** Save a draft owned by the current user. */
    @Transactional
    public EmailDetail saveDraft(User owner, ComposeEmailRequest req) {
        Email draft = new Email();
        draft.setOwner(owner);
        draft.setFromAddr(owner.getEmail());
        draft.setToAddrs(safe(req.getTo()));
        draft.setCc(safe(req.getCc()));
        draft.setBcc(safe(req.getBcc()));
        draft.setSubject(safe(req.getSubject()));
        draft.setBodyHtml(req.getBodyHtml());
        draft.setBodyText(req.getBodyText());
        draft.setThreadId(req.getThreadId() != null ? req.getThreadId() : UUID.randomUUID().toString());
        draft.setFolder(Email.Folder.DRAFTS);
        draft.setRead(true);
        draft.setArchived(false);
        draft.setDeleted(false);
        draft = emails.save(draft);
        return toDetail(draft);
    }

    // PUBLIC_INTERFACE
    /** Compose and send an email. Creates a 'sent' copy for sender and 'inbox' copies for recipients within the same DB (internal delivery). */
    @Transactional
    public EmailDetail composeAndSend(User sender, ComposeEmailRequest req) {
        Instant now = Instant.now();
        String thread = req.getThreadId() != null && !req.getThreadId().isBlank() ? req.getThreadId() : UUID.randomUUID().toString();

        // Sender copy in SENT
        Email sent = new Email();
        sent.setOwner(sender);
        sent.setFromAddr(sender.getEmail());
        sent.setToAddrs(safe(req.getTo()));
        sent.setCc(safe(req.getCc()));
        sent.setBcc(safe(req.getBcc()));
        sent.setSubject(safe(req.getSubject()));
        sent.setBodyHtml(req.getBodyHtml());
        sent.setBodyText(req.getBodyText());
        sent.setThreadId(thread);
        sent.setFolder(Email.Folder.SENT);
        sent.setRead(true);
        sent.setSentAt(now);
        sent.setDeleted(false);
        sent.setArchived(false);
        emails.save(sent);

        // Deliver to recipients internally (only to registered users)
        deliverToRecipients(sender, req, now, thread);

        return toDetail(sent);
    }

    private void deliverToRecipients(User sender, ComposeEmailRequest req, Instant now, String thread) {
        String to = safe(req.getTo());
        if (to.isBlank()) {
            return;
        }
        for (String address : to.split(",")) {
            String emailAddr = address.trim().toLowerCase();
            if (emailAddr.isBlank()) continue;

            users.findByEmail(emailAddr).ifPresent(recipient -> {
                Email inbox = new Email();
                inbox.setOwner(recipient);
                inbox.setFromAddr(sender.getEmail());
                inbox.setToAddrs(to);
                inbox.setCc(safe(req.getCc()));
                inbox.setBcc(null); // Bcc not visible to recipient
                inbox.setSubject(safe(req.getSubject()));
                inbox.setBodyHtml(req.getBodyHtml());
                inbox.setBodyText(req.getBodyText());
                inbox.setThreadId(thread);
                inbox.setFolder(Email.Folder.INBOX);
                inbox.setRead(false);
                inbox.setReceivedAt(now);
                inbox.setDeleted(false);
                inbox.setArchived(false);
                emails.save(inbox);
            });
        }
    }

    // PUBLIC_INTERFACE
    /** Update flags for an email belonging to the owner. */
    @Transactional
    public Optional<EmailDetail> updateFlags(User owner, Long id, UpdateFlagsRequest req) {
        return emails.findById(id)
                .filter(e -> e.getOwner().getId().equals(owner.getId()))
                .map(e -> {
                    if (req.getRead() != null) e.setRead(req.getRead());
                    if (req.getStarred() != null) e.setStarred(req.getStarred());
                    if (req.getArchived() != null) {
                        e.setArchived(req.getArchived());
                        if (req.getArchived()) {
                            e.setFolder(Email.Folder.ARCHIVED);
                        } else if (!e.isDeleted()) {
                            // move back to inbox if was archived and not deleted and receivedAt exists else sent
                            e.setFolder(e.getSentAt() != null ? Email.Folder.SENT : Email.Folder.INBOX);
                        }
                    }
                    if (req.getDeleted() != null) {
                        e.setDeleted(req.getDeleted());
                        if (req.getDeleted()) {
                            e.setFolder(Email.Folder.TRASH);
                        } else {
                            // restore
                            if (e.isArchived()) {
                                e.setFolder(Email.Folder.ARCHIVED);
                            } else {
                                e.setFolder(e.getSentAt() != null ? Email.Folder.SENT : Email.Folder.INBOX);
                            }
                        }
                    }
                    return emails.save(e);
                })
                .map(this::toDetail);
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private EmailListItem toListItem(Email e) {
        String preview = e.getBodyText();
        if (preview == null || preview.isBlank()) {
            preview = e.getBodyHtml() != null ? e.getBodyHtml().replaceAll("<[^>]*>", "").trim() : "";
        }
        if (preview.length() > 140) {
            preview = preview.substring(0, 140) + "...";
        }
        return new EmailListItem(
                e.getId(),
                e.getFromAddr(),
                e.getToAddrs(),
                e.getSubject(),
                preview,
                e.isRead(),
                e.isStarred(),
                e.isArchived(),
                e.isDeleted(),
                e.getUpdatedAt(),
                e.getReceivedAt(),
                e.getSentAt()
        );
    }

    private EmailDetail toDetail(Email e) {
        return new EmailDetail(
                e.getId(),
                e.getFromAddr(),
                e.getToAddrs(),
                e.getCc(),
                e.getBcc(),
                e.getSubject(),
                e.getBodyHtml(),
                e.getBodyText(),
                e.isRead(),
                e.isStarred(),
                e.isArchived(),
                e.isDeleted(),
                e.getThreadId(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getSentAt(),
                e.getReceivedAt()
        );
    }
}
