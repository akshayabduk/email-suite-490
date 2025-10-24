package com.example.demo.label.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * Request DTO to assign a set of labels to an email (adds).
 */
public class AssignLabelsRequest {

    @Schema(description = "List of label ids to assign to the email")
    @NotNull
    private List<Long> labelIds;

    public AssignLabelsRequest() {}

    public AssignLabelsRequest(List<Long> labelIds) { this.labelIds = labelIds; }

    // PUBLIC_INTERFACE
    public List<Long> getLabelIds() { return labelIds; }
    public void setLabelIds(List<Long> labelIds) { this.labelIds = labelIds; }
}
