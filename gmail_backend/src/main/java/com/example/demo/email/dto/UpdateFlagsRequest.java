package com.example.demo.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * PUBLIC_INTERFACE
 * DTO for flag update actions like read/star/archive/delete/restore.
 * Each non-null field will be applied.
 */
public class UpdateFlagsRequest {

    @Schema(description = "Mark email as read/unread", example = "true")
    private Boolean read;

    @Schema(description = "Star/unstar email", example = "true")
    private Boolean starred;

    @Schema(description = "Archive/unarchive email", example = "true")
    private Boolean archived;

    @Schema(description = "Delete/undelete (restore) email", example = "true")
    private Boolean deleted;

    public UpdateFlagsRequest() {}

    // PUBLIC_INTERFACE
    public Boolean getRead() { return read; }
    // PUBLIC_INTERFACE
    public Boolean getStarred() { return starred; }
    // PUBLIC_INTERFACE
    public Boolean getArchived() { return archived; }
    // PUBLIC_INTERFACE
    public Boolean getDeleted() { return deleted; }

    public void setRead(Boolean read) { this.read = read; }
    public void setStarred(Boolean starred) { this.starred = starred; }
    public void setArchived(Boolean archived) { this.archived = archived; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }
}
