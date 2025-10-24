package com.example.demo.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * PUBLIC_INTERFACE
 * Full email details for a single message view.
 */
public class EmailDetail {

    @Schema(description = "Email id")
    private Long id;

    @Schema(description = "From address")
    private String from;

    @Schema(description = "To addresses")
    private String to;

    @Schema(description = "Cc addresses")
    private String cc;

    @Schema(description = "Bcc addresses")
    private String bcc;

    @Schema(description = "Subject")
    private String subject;

    @Schema(description = "Body HTML")
    private String bodyHtml;

    @Schema(description = "Body text")
    private String bodyText;

    @Schema(description = "Read flag")
    private boolean read;

    @Schema(description = "Starred flag")
    private boolean starred;

    @Schema(description = "Archived flag")
    private boolean archived;

    @Schema(description = "Deleted flag")
    private boolean deleted;

    @Schema(description = "Thread id")
    private String threadId;

    @Schema(description = "Created timestamp")
    private Instant createdAt;

    @Schema(description = "Updated timestamp")
    private Instant updatedAt;

    @Schema(description = "Sent timestamp")
    private Instant sentAt;

    @Schema(description = "Received timestamp")
    private Instant receivedAt;

    public EmailDetail() {}

    public EmailDetail(Long id, String from, String to, String cc, String bcc, String subject, String bodyHtml, String bodyText, boolean read, boolean starred, boolean archived, boolean deleted, String threadId, Instant createdAt, Instant updatedAt, Instant sentAt, Instant receivedAt) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.bodyHtml = bodyHtml;
        this.bodyText = bodyText;
        this.read = read;
        this.starred = starred;
        this.archived = archived;
        this.deleted = deleted;
        this.threadId = threadId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sentAt = sentAt;
        this.receivedAt = receivedAt;
    }

    // PUBLIC_INTERFACE
    public Long getId() { return id; }
    // PUBLIC_INTERFACE
    public String getFrom() { return from; }
    // PUBLIC_INTERFACE
    public String getTo() { return to; }
    // PUBLIC_INTERFACE
    public String getCc() { return cc; }
    // PUBLIC_INTERFACE
    public String getBcc() { return bcc; }
    // PUBLIC_INTERFACE
    public String getSubject() { return subject; }
    // PUBLIC_INTERFACE
    public String getBodyHtml() { return bodyHtml; }
    // PUBLIC_INTERFACE
    public String getBodyText() { return bodyText; }
    // PUBLIC_INTERFACE
    public boolean isRead() { return read; }
    // PUBLIC_INTERFACE
    public boolean isStarred() { return starred; }
    // PUBLIC_INTERFACE
    public boolean isArchived() { return archived; }
    // PUBLIC_INTERFACE
    public boolean isDeleted() { return deleted; }
    // PUBLIC_INTERFACE
    public String getThreadId() { return threadId; }
    // PUBLIC_INTERFACE
    public Instant getCreatedAt() { return createdAt; }
    // PUBLIC_INTERFACE
    public Instant getUpdatedAt() { return updatedAt; }
    // PUBLIC_INTERFACE
    public Instant getSentAt() { return sentAt; }
    // PUBLIC_INTERFACE
    public Instant getReceivedAt() { return receivedAt; }

    public void setId(Long id) { this.id = id; }
    public void setFrom(String from) { this.from = from; }
    public void setTo(String to) { this.to = to; }
    public void setCc(String cc) { this.cc = cc; }
    public void setBcc(String bcc) { this.bcc = bcc; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setBodyHtml(String bodyHtml) { this.bodyHtml = bodyHtml; }
    public void setBodyText(String bodyText) { this.bodyText = bodyText; }
    public void setRead(boolean read) { this.read = read; }
    public void setStarred(boolean starred) { this.starred = starred; }
    public void setArchived(boolean archived) { this.archived = archived; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    public void setThreadId(String threadId) { this.threadId = threadId; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }
    public void setReceivedAt(Instant receivedAt) { this.receivedAt = receivedAt; }
}
