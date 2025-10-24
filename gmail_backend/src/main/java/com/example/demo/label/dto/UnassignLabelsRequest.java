package com.example.demo.label.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * Request DTO to unassign a set of labels from an email (removes).
 */
public class UnassignLabelsRequest {

    @Schema(description = "List of label ids to remove from the email")
    @NotNull
    private List<Long> labelIds;

    public UnassignLabelsRequest() {}

    public UnassignLabelsRequest(List<Long> labelIds) { this.labelIds = labelIds; }

    // PUBLIC_INTERFACE
    public List<Long> getLabelIds() { return labelIds; }
    public void setLabelIds(List<Long> labelIds) { this.labelIds = labelIds; }
}
