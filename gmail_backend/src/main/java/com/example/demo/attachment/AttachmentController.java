package com.example.demo.attachment;

import com.example.demo.attachment.dto.AttachmentDto;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * PUBLIC_INTERFACE
 * REST controller for attachments:
 * - POST /api/v1/attachments/upload (multipart) with emailId
 * - GET /api/v1/attachments/{id}/download returns Content-Disposition: attachment
 * - GET /api/v1/attachments/email/{emailId} list attachments for email
 */
@RestController
@RequestMapping("/api/v1/attachments")
@Tag(name = "Attachments", description = "Upload, download, list email attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final UserRepository users;

    public AttachmentController(AttachmentService attachmentService, UserRepository users) {
        this.attachmentService = attachmentService;
        this.users = users;
    }

    private User currentUser(Authentication auth) {
        if (auth == null || auth.getName() == null) return null;
        return users.findByEmail(auth.getName()).orElse(null);
    }

    // PUBLIC_INTERFACE
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload attachments", description = "Uploads one or more files and attaches them to the specified email owned by current user")
    public ResponseEntity<List<AttachmentDto>> upload(Authentication auth,
                                                      @RequestParam("emailId") @Parameter(description = "Target email id") Long emailId,
                                                      @RequestPart("files") List<MultipartFile> files) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        try {
            List<AttachmentDto> dtos = attachmentService.upload(u, emailId, files);
            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(400).build();
        }
    }

    // PUBLIC_INTERFACE
    @GetMapping("/{id}/download")
    @Operation(summary = "Download attachment", description = "Downloads an attachment if owned by current user; returns Content-Disposition header")
    public ResponseEntity<Resource> download(Authentication auth,
                                             @PathVariable("id") Long id) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();

        var resOpt = attachmentService.loadForDownload(u, id);
        if (resOpt.isEmpty() || resOpt.get() == null) {
            return ResponseEntity.status(404).build();
        }
        var dl = resOpt.get();
        String encoded = URLEncoder.encode(dl.getFilename(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded);
        // Expose Content-Disposition for browsers over CORS
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(dl.getContentType());
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(mediaType)
                .contentLength(dl.getSize())
                .body(dl.getResource());
    }

    // PUBLIC_INTERFACE
    @GetMapping("/email/{emailId}")
    @Operation(summary = "List attachments by email", description = "Returns list of attachments for the given email owned by current user")
    public ResponseEntity<List<AttachmentDto>> list(Authentication auth,
                                                    @PathVariable("emailId") Long emailId) {
        User u = currentUser(auth);
        if (u == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(attachmentService.listByEmail(u, emailId));
    }
}
