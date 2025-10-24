package com.example.demo.email;

import com.example.demo.email.dto.ComposeEmailRequest;
import com.example.demo.email.dto.EmailDetail;
import com.example.demo.email.dto.EmailListItem;
import com.example.demo.email.dto.UpdateFlagsRequest;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * PUBLIC_INTERFACE
 * REST controller for email operations (inbox/sent/drafts/trash/archived, search, compose, flags).
 */
@RestController
@RequestMapping("/api/v1/emails")
@Tag(name = "Emails", description = "Email operations API")
public class EmailController {

    private final EmailService emailService;
    private final UserRepository users;

    public EmailController(EmailService emailService, UserRepository users) {
        this.emailService = emailService;
        this.users = users;
    }

    private User currentUser(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return null;
        }
        return users.findByEmail(auth.getName()).orElse(null);
    }

    // PUBLIC_INTERFACE
    @GetMapping("/inbox")
    @Operation(summary = "List inbox", description = "Returns paginated inbox for current user")
    public ResponseEntity<Page<EmailListItem>> inbox(Authentication auth,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size)));
        return ResponseEntity.ok(emailService.listInbox(u, pageable));
    }

    // PUBLIC_INTERFACE
    @GetMapping("/sent")
    @Operation(summary = "List sent", description = "Returns paginated sent emails for current user")
    public ResponseEntity<Page<EmailListItem>> sent(Authentication auth,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "20") int size) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size)));
        return ResponseEntity.ok(emailService.listSent(u, pageable));
    }

    // PUBLIC_INTERFACE
    @GetMapping("/drafts")
    @Operation(summary = "List drafts", description = "Returns paginated drafts for current user")
    public ResponseEntity<Page<EmailListItem>> drafts(Authentication auth,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size)));
        return ResponseEntity.ok(emailService.listDrafts(u, pageable));
    }

    // PUBLIC_INTERFACE
    @GetMapping("/trash")
    @Operation(summary = "List trash", description = "Returns paginated trash for current user")
    public ResponseEntity<Page<EmailListItem>> trash(Authentication auth,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size)));
        return ResponseEntity.ok(emailService.listTrash(u, pageable));
    }

    // PUBLIC_INTERFACE
    @GetMapping("/archived")
    @Operation(summary = "List archived", description = "Returns paginated archived for current user")
    public ResponseEntity<Page<EmailListItem>> archived(Authentication auth,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size)));
        return ResponseEntity.ok(emailService.listArchived(u, pageable));
    }

    // PUBLIC_INTERFACE
    @GetMapping("/{id}")
    @Operation(summary = "Get email details", description = "Fetches a single email details owned by the current user")
    public ResponseEntity<EmailDetail> get(Authentication auth,
                                           @PathVariable Long id) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        return emailService.getEmail(u, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    // PUBLIC_INTERFACE
    @GetMapping("/search")
    @Operation(summary = "Search emails", description = "Fulltext-like search across subject and body for current user")
    public ResponseEntity<Page<EmailListItem>> search(Authentication auth,
                                                      @RequestParam("q") @Parameter(description = "Query string") String q,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size)));
        return ResponseEntity.ok(emailService.search(u, q, pageable));
    }

    // PUBLIC_INTERFACE
    @PostMapping("/compose")
    @Operation(
            summary = "Compose and send email",
            description = "Sends an email and stores a SENT copy for the sender and INBOX copies for internal recipients.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email sent", content = @Content(schema = @Schema(implementation = EmailDetail.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    public ResponseEntity<EmailDetail> compose(Authentication auth, @Valid @RequestBody ComposeEmailRequest request) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        if (request.getTo() == null || request.getTo().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(emailService.composeAndSend(u, request));
    }

    // PUBLIC_INTERFACE
    @PostMapping("/draft")
    @Operation(summary = "Save draft", description = "Saves a draft for the current user and returns its details")
    public ResponseEntity<EmailDetail> draft(Authentication auth, @Valid @RequestBody ComposeEmailRequest request) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(emailService.saveDraft(u, request));
    }

    // PUBLIC_INTERFACE
    @PatchMapping("/{id}/flags")
    @Operation(summary = "Update flags", description = "Updates read/star/archive/delete flags for the given email")
    public ResponseEntity<EmailDetail> updateFlags(Authentication auth,
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody UpdateFlagsRequest request) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        return emailService.updateFlags(u, id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }
}
