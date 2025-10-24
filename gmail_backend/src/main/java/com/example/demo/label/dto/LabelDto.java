package com.example.demo.label.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * PUBLIC_INTERFACE
 * DTO representing a label.
 */
public class LabelDto {

    @Schema(description = "Label id", example = "1")
    private Long id;

    @Schema(description = "Label name", example = "Work")
    private String name;

    @Schema(description = "Hex color", example = "#2563EB")
    private String color;

    @Schema(description = "Created timestamp")
    private Instant createdAt;

    @Schema(description = "Updated timestamp")
    private Instant updatedAt;

    public LabelDto() {}

    public LabelDto(Long id, String name, String color, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // PUBLIC_INTERFACE
    public Long getId() { return id; }
    // PUBLIC_INTERFACE
    public String getName() { return name; }
    // PUBLIC_INTERFACE
    public String getColor() { return color; }
    // PUBLIC_INTERFACE
    public Instant getCreatedAt() { return createdAt; }
    // PUBLIC_INTERFACE
    public Instant getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setColor(String color) { this.color = color; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
