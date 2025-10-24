package com.example.demo.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * PUBLIC_INTERFACE
 * Lightweight list item representation for an email.
 */
public class EmailListItem {

    @Schema(description = "Email id", example = "42")
    private Long id;

    @Schema(description = "From address", example = "alice@example.com")
    private String from;

    @Schema(description = "To addresses (comma-separated)", example = "bob@example.com,charlie@example.com")
    private String to;

    @Schema(description = "Subject", example = "Project Update")
    private String subject;

    @Schema(description = "Preview text snippet", example = "Here is a quick update...")
    private String preview;

    @Schema(description = "Read flag")
    private boolean read;

    @Schema(description = "Starred flag")
    private boolean starred;

    @Schema(description = "Archived flag")
    private boolean archived;

    @Schema(description = "Deleted flag")
    private boolean deleted;

    @Schema(description = "Updated timestamp")
    private Instant updatedAt;

    @Schema(description = "Received timestamp if applicable")
    private Instant receivedAt;

    @Schema(description = "Sent timestamp if applicable")
    private Instant sentAt;

    public EmailListItem() {
    }

    public EmailListItem(Long id, String from, String to, String subject, String preview, boolean read, boolean starred, boolean archived, boolean deleted, Instant updatedAt, Instant receivedAt, Instant sentAt) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.preview = preview;
        this.read = read;
        this.starred = starred;
        this.archived = archived;
        this.deleted = deleted;
        this.updatedAt = updatedAt;
        this.receivedAt = receivedAt;
        this.sentAt = sentAt;
    }

    // PUBLIC_INTERFACE
    public Long getId() {
        return id;
    }

    // PUBLIC_INTERFACE
    public String getFrom() {
        return from;
    }

    // PUBLIC_INTERFACE
    public String getTo() {
        return to;
    }

    // PUBLIC_INTERFACE
    public String getSubject() {
        return subject;
    }

    // PUBLIC_INTERFACE
    public String getPreview() {
        return preview;
    }

    // PUBLIC_INTERFACE
    public boolean isRead() {
        return read;
    }

    // PUBLIC_INTERFACE
    public boolean isStarred() {
        return starred;
    }

    // PUBLIC_INTERFACE
    public boolean isArchived() {
        return archived;
    }

    // PUBLIC_INTERFACE
    public boolean isDeleted() {
        return deleted;
    }

    // PUBLIC_INTERFACE
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // PUBLIC_INTERFACE
    public Instant getReceivedAt() {
        return receivedAt;
    }

    // PUBLIC_INTERFACE
    public Instant getSentAt() {
        return sentAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }
}
