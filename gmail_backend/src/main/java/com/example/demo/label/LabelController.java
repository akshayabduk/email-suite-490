package com.example.demo.label;

import com.example.demo.email.dto.EmailListItem;
import com.example.demo.label.dto.*;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * REST controller for label CRUD and assignment operations.
 */
@RestController
@RequestMapping("/api/v1/labels")
@Tag(name = "Labels", description = "User label management and assignment API")
public class LabelController {

    private final LabelService labelService;
    private final UserRepository users;

    public LabelController(LabelService labelService, UserRepository users) {
        this.labelService = labelService;
        this.users = users;
    }

    private User currentUser(Authentication auth) {
        if (auth == null || auth.getName() == null) return null;
        return users.findByEmail(auth.getName()).orElse(null);
    }

    // PUBLIC_INTERFACE
    @GetMapping
    @Operation(summary = "List labels", description = "Returns paginated labels of current user")
    public ResponseEntity<Page<LabelDto>> list(Authentication auth,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "50") int size) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(200, Math.max(1, size)));
        return ResponseEntity.ok(labelService.list(u, pageable));
    }

    // PUBLIC_INTERFACE
    @PostMapping
    @Operation(summary = "Create label", description = "Creates a new label for current user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Created", content = @Content(schema = @Schema(implementation = LabelDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid or duplicate name")
            })
    public ResponseEntity<LabelDto> create(Authentication auth, @Valid @RequestBody CreateLabelRequest req) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        return labelService.create(u, req)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    // PUBLIC_INTERFACE
    @PatchMapping("/{id}")
    @Operation(summary = "Update label", description = "Updates name and/or color of a label owned by the user")
    public ResponseEntity<LabelDto> update(Authentication auth,
                                           @PathVariable Long id,
                                           @Valid @RequestBody UpdateLabelRequest req) {
        User u = currentUser(auth);
        if (u == null) {
            return ResponseEntity.status(401).build();
        }
        // Perform update and handle three outcomes: not found (404), name conflict/invalid (400), success (200)
        var updateOpt = labelService.update(u, id, req);
        if (updateOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        LabelDto dto = updateOpt.get();
        if (dto == null) {
            // Service uses null to indicate name conflict or invalid update
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(dto);
    }

    // PUBLIC_INTERFACE
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete label", description = "Deletes a label and removes it from all emails")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        boolean ok = labelService.delete(u, id);
        if (!ok) return ResponseEntity.status(404).build();
        return ResponseEntity.noContent().build();
    }

    // PUBLIC_INTERFACE
    @PostMapping("/emails/{emailId}/assign")
    @Operation(summary = "Assign labels to an email",
            description = "Assigns given labels (ids) to the specified email owned by user")
    public ResponseEntity<Void> assign(Authentication auth,
                                       @PathVariable Long emailId,
                                       @Valid @RequestBody AssignLabelsRequest req) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        if (req.getLabelIds() == null || req.getLabelIds().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        boolean ok = labelService.assignLabels(u, emailId, req.getLabelIds());
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(404).build();
    }

    // PUBLIC_INTERFACE
    @PostMapping("/emails/{emailId}/unassign")
    @Operation(summary = "Unassign labels from an email",
            description = "Removes given labels (ids) from the specified email owned by user")
    public ResponseEntity<Void> unassign(Authentication auth,
                                         @PathVariable Long emailId,
                                         @Valid @RequestBody UnassignLabelsRequest req) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        if (req.getLabelIds() == null || req.getLabelIds().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        boolean ok = labelService.unassignLabels(u, emailId, req.getLabelIds());
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(404).build();
    }

    // PUBLIC_INTERFACE
    @GetMapping("/{labelId}/emails")
    @Operation(summary = "List emails by label", description = "Returns paginated emails for the current user that are assigned the given label")
    public ResponseEntity<Page<EmailListItem>> emailsByLabel(Authentication auth,
                                                             @PathVariable @Parameter(description = "Label id") Long labelId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int size) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size)));
        return ResponseEntity.ok(labelService.listEmailsByLabel(u, labelId, pageable));
    }
}
