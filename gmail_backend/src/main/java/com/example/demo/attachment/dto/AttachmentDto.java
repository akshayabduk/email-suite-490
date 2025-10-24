package com.example.demo.attachment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * PUBLIC_INTERFACE
 * DTO representing attachment metadata for listing.
 */
public class AttachmentDto {

    @Schema(description = "Attachment id", example = "10")
    private Long id;

    @Schema(description = "Email id this attachment belongs to", example = "42")
    private Long emailId;

    @Schema(description = "Original filename", example = "notes.txt")
    private String filename;

    @Schema(description = "Content type", example = "text/plain")
    private String contentType;

    @Schema(description = "Size in bytes", example = "5120")
    private long size;

    @Schema(description = "Created timestamp")
    private Instant createdAt;

    public AttachmentDto() {}

    public AttachmentDto(Long id, Long emailId, String filename, String contentType, long size, Instant createdAt) {
        this.id = id;
        this.emailId = emailId;
        this.filename = filename;
        this.contentType = contentType;
        this.size = size;
        this.createdAt = createdAt;
    }

    // PUBLIC_INTERFACE
    public Long getId() { return id; }
    // PUBLIC_INTERFACE
    public Long getEmailId() { return emailId; }
    // PUBLIC_INTERFACE
    public String getFilename() { return filename; }
    // PUBLIC_INTERFACE
    public String getContentType() { return contentType; }
    // PUBLIC_INTERFACE
    public long getSize() { return size; }
    // PUBLIC_INTERFACE
    public Instant getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setEmailId(Long emailId) { this.emailId = emailId; }
    public void setFilename(String filename) { this.filename = filename; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setSize(long size) { this.size = size; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
