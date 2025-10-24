package com.example.demo.label.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * PUBLIC_INTERFACE
 * Request DTO for creating a label.
 */
public class CreateLabelRequest {

    @Schema(description = "Label name (unique per user)", example = "Work")
    @NotBlank
    @Size(max = 120)
    private String name;

    @Schema(description = "Optional color hex", example = "#2563EB")
    @Size(max = 10)
    private String color;

    public CreateLabelRequest() {}

    public CreateLabelRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    // PUBLIC_INTERFACE
    public String getName() { return name; }
    // PUBLIC_INTERFACE
    public String getColor() { return color; }
    public void setName(String name) { this.name = name; }
    public void setColor(String color) { this.color = color; }
}
