package com.example.demo.attachment;

import com.example.demo.email.Email;
import com.example.demo.email.EmailRepository;
import com.example.demo.attachment.dto.AttachmentDto;
import com.example.demo.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * PUBLIC_INTERFACE
 * Service for uploading, listing, and downloading attachments.
 * Stores files on disk under FILE_STORAGE_DIR and persists metadata in DB.
 */
@Service
public class AttachmentService {

    private final AttachmentRepository attachments;
    private final EmailRepository emails;

    private final Path storageRoot;

    public AttachmentService(AttachmentRepository attachments,
                             EmailRepository emails,
                             @Value("${file.storage.dir:}") String storageDirFromEnv) throws IOException {
        this.attachments = attachments;
        this.emails = emails;

        // Determine storage root: env FILE_STORAGE_DIR or default to <project-root>/attachments
        String dir = storageDirFromEnv;
        if (!StringUtils.hasText(dir)) {
            // Safe default relative to working directory
            dir = "attachments";
        }
        this.storageRoot = Paths.get(dir).toAbsolutePath().normalize();
        Files.createDirectories(this.storageRoot);
    }

    // PUBLIC_INTERFACE
    /**
     * Uploads a list of files for an email owned by the user.
     */
    @Transactional
    public List<AttachmentDto> upload(User owner, Long emailId, List<MultipartFile> files) throws IOException {
        Email email = emails.findById(emailId)
                .filter(e -> e.getOwner().getId().equals(owner.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Email not found or not owned"));

        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("No files to upload");
        }

        List<AttachmentDto> result = new java.util.ArrayList<>();
        for (MultipartFile mf : files) {
            if (mf.isEmpty()) continue;

            String safeOriginal = Paths.get(mf.getOriginalFilename() == null ? "file.bin" : mf.getOriginalFilename())
                    .getFileName().toString();
            String ext = "";
            int idx = safeOriginal.lastIndexOf('.');
            if (idx > 0 && idx < safeOriginal.length() - 1) {
                ext = safeOriginal.substring(idx);
            }
            String randomName = UUID.randomUUID().toString().replace("-", "") + ext;

            // Structure: <root>/<ownerId>/<emailId>/<randomName>
            Path targetDir = storageRoot.resolve(owner.getId().toString()).resolve(email.getId().toString());
            Files.createDirectories(targetDir);
            Path target = targetDir.resolve(randomName);

            // Write file atomically
            Path tmp = Files.createTempFile(targetDir, "up-", ".tmp");
            Files.write(tmp, mf.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);

            String contentType = mf.getContentType() != null ? mf.getContentType() : "application/octet-stream";
            long size = Files.size(target);
            String storagePath = storageRoot.relativize(target).toString().replace('\\', '/');

            Attachment att = new Attachment(owner, email, safeOriginal, contentType, size, storagePath);
            att = attachments.save(att);

            result.add(toDto(att));
        }
        return result;
    }

    // PUBLIC_INTERFACE
    /**
     * Lists attachments for an email owned by the user.
     */
    @Transactional(readOnly = true)
    public List<AttachmentDto> listByEmail(User owner, Long emailId) {
        Optional<Email> emailOpt = emails.findById(emailId)
                .filter(e -> e.getOwner().getId().equals(owner.getId()));
        if (emailOpt.isEmpty()) return List.of();
        return attachments.findByOwnerAndEmailOrderByCreatedAtAsc(owner, emailOpt.get())
                .stream().map(this::toDto).toList();
    }

    // PUBLIC_INTERFACE
    /**
     * Loads an attachment resource for download if owned by user.
     */
    @Transactional(readOnly = true)
    public Optional<DownloadResource> loadForDownload(User owner, Long id) {
        return attachments.findByIdAndOwner(id, owner).map(att -> {
            Path path = storageRoot.resolve(att.getStoragePath()).normalize();
            Resource resource = new FileSystemResource(path.toFile());
            if (!resource.exists() || !resource.isReadable()) {
                return null;
            }
            return new DownloadResource(att.getOriginalFilename(), att.getContentType(), att.getSize(), resource, att.getCreatedAt());
        });
    }

    private AttachmentDto toDto(Attachment a) {
        return new AttachmentDto(a.getId(),
                a.getEmail().getId(),
                a.getOriginalFilename(),
                a.getContentType(),
                a.getSize(),
                a.getCreatedAt());
    }

    /**
     * Helper record for download response.
     */
    public static class DownloadResource {
        private final String filename;
        private final String contentType;
        private final long size;
        private final Resource resource;
        private final Instant createdAt;

        public DownloadResource(String filename, String contentType, long size, Resource resource, Instant createdAt) {
            this.filename = filename;
            this.contentType = contentType;
            this.size = size;
            this.resource = resource;
            this.createdAt = createdAt;
        }

        // PUBLIC_INTERFACE
        public String getFilename() { return filename; }
        // PUBLIC_INTERFACE
        public String getContentType() { return contentType; }
        // PUBLIC_INTERFACE
        public long getSize() { return size; }
        // PUBLIC_INTERFACE
        public Resource getResource() { return resource; }
        // PUBLIC_INTERFACE
        public Instant getCreatedAt() { return createdAt; }
    }
}
