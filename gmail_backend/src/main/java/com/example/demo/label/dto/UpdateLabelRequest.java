package com.example.demo.label.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * PUBLIC_INTERFACE
 * Request DTO for updating a label (partial).
 */
public class UpdateLabelRequest {

    @Schema(description = "New label name", example = "Personal")
    @Size(max = 120)
    private String name;

    @Schema(description = "New color hex", example = "#F59E0B")
    @Size(max = 10)
    private String color;

    public UpdateLabelRequest() {}

    // PUBLIC_INTERFACE
    public String getName() { return name; }
    // PUBLIC_INTERFACE
    public String getColor() { return color; }
    public void setName(String name) { this.name = name; }
    public void setColor(String color) { this.color = color; }
}
