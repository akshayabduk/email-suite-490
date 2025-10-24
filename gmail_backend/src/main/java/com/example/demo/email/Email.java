package com.example.demo.email;

import com.example.demo.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

/**
 * PUBLIC_INTERFACE
 * JPA Entity representing an Email message stored in the system.
 * Supports inbox/sent/drafts/trash/archived via flags and folder fields.
 */
@Entity
@Table(name = "emails", indexes = {
        @Index(name = "idx_emails_owner", columnList = "owner_id"),
        @Index(name = "idx_emails_thread", columnList = "threadId"),
        @Index(name = "idx_emails_deleted_archived", columnList = "isDeleted,isArchived"),
        @Index(name = "idx_emails_sent_received", columnList = "sentAt,receivedAt")
})
public class Email {

    public enum Folder {
        INBOX, SENT, DRAFTS, TRASH, ARCHIVED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The mailbox owner (currently authenticated user's mailbox)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_emails_owner"))
    private User owner;

    @NotBlank
    @Size(max = 190)
    @Column(nullable = false, length = 190)
    private String fromAddr;

    // Comma-separated lists for recipients
    @Size(max = 2000)
    @Column(length = 2000)
    private String toAddrs;

    @Size(max = 2000)
    @Column(length = 2000)
    private String cc;

    @Size(max = 2000)
    @Column(length = 2000)
    private String bcc;

    @Size(max = 500)
    @Column(length = 500)
    private String subject;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String bodyHtml;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String bodyText;

    @Column(nullable = false)
    private boolean isRead = false;

    @Column(nullable = false)
    private boolean isStarred = false;

    @Column(nullable = false)
    private boolean isArchived = false;

    @Column(nullable = false)
    private boolean isDeleted = false;

    // Indicates in which logical folder the email currently resides for this owner
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Folder folder = Folder.INBOX;

    private Instant sentAt;
    private Instant receivedAt;

    @Size(max = 190)
    @Column(length = 190)
    private String threadId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (folder == null) {
            folder = Folder.INBOX;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public Email() {
    }

    // PUBLIC_INTERFACE
    /** Primary key of the email row. */
    public Long getId() {
        return id;
    }

    // PUBLIC_INTERFACE
    /** Owner mailbox user for this email row. */
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    // PUBLIC_INTERFACE
    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    // PUBLIC_INTERFACE
    public String getToAddrs() {
        return toAddrs;
    }

    public void setToAddrs(String toAddrs) {
        this.toAddrs = toAddrs;
    }

    // PUBLIC_INTERFACE
    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    // PUBLIC_INTERFACE
    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    // PUBLIC_INTERFACE
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    // PUBLIC_INTERFACE
    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    // PUBLIC_INTERFACE
    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    // PUBLIC_INTERFACE
    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    // PUBLIC_INTERFACE
    public boolean isStarred() {
        return isStarred;
    }

    public void setStarred(boolean starred) {
        isStarred = starred;
    }

    // PUBLIC_INTERFACE
    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    // PUBLIC_INTERFACE
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    // PUBLIC_INTERFACE
    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    // PUBLIC_INTERFACE
    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    // PUBLIC_INTERFACE
    public Instant getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    // PUBLIC_INTERFACE
    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    // PUBLIC_INTERFACE
    public Instant getCreatedAt() {
        return createdAt;
    }

    // PUBLIC_INTERFACE
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
