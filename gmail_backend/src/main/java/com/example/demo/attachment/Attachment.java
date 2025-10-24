package com.example.demo.attachment;

import com.example.demo.email.Email;
import com.example.demo.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.Instant;

/**
 * PUBLIC_INTERFACE
 * JPA Entity for email attachments metadata stored in DB; binary stored on disk.
 */
@Entity
@Table(name = "attachments",
        indexes = {
                @Index(name = "idx_attachment_owner", columnList = "owner_id"),
                @Index(name = "idx_attachment_email", columnList = "email_id")
        })
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Attachment id")
    private Long id;

    // Owner to enforce user-scoped security
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_attachment_owner"))
    private User owner;

    // Email to which the attachment belongs
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id", nullable = false, foreignKey = @ForeignKey(name = "fk_attachment_email"))
    private Email email;

    @Column(nullable = false, length = 300)
    @Schema(description = "Original filename", example = "document.pdf")
    private String originalFilename;

    @Column(nullable = false, length = 200)
    @Schema(description = "MIME content type", example = "application/pdf")
    private String contentType;

    @Column(nullable = false)
    @Schema(description = "File size in bytes", example = "102400")
    private long size;

    @Column(nullable = false, length = 200)
    @Schema(description = "Disk storage path relative to storage root")
    private String storagePath;

    @Column(nullable = false, updatable = false)
    @Schema(description = "Creation timestamp")
    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
    }

    public Attachment() {}

    public Attachment(User owner, Email email, String originalFilename, String contentType, long size, String storagePath) {
        this.owner = owner;
        this.email = email;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.size = size;
        this.storagePath = storagePath;
    }

    // PUBLIC_INTERFACE
    public Long getId() { return id; }
    // PUBLIC_INTERFACE
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    // PUBLIC_INTERFACE
    public Email getEmail() { return email; }
    public void setEmail(Email email) { this.email = email; }
    // PUBLIC_INTERFACE
    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }
    // PUBLIC_INTERFACE
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    // PUBLIC_INTERFACE
    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }
    // PUBLIC_INTERFACE
    public String getStoragePath() { return storagePath; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }
    // PUBLIC_INTERFACE
    public Instant getCreatedAt() { return createdAt; }
}
