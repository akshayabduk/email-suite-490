package com.example.demo.label;

import com.example.demo.email.Email;
import com.example.demo.email.EmailRepository;
import com.example.demo.email.dto.EmailListItem;
import com.example.demo.label.dto.CreateLabelRequest;
import com.example.demo.label.dto.LabelDto;
import com.example.demo.label.dto.UpdateLabelRequest;
import com.example.demo.user.User;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * PUBLIC_INTERFACE
 * Service encapsulating label CRUD and email-label assignment operations.
 */
@Service
public class LabelService {

    private final LabelRepository labels;
    private final EmailLabelRepository emailLabels;
    private final EmailRepository emails;

    public LabelService(LabelRepository labels, EmailLabelRepository emailLabels, EmailRepository emails) {
        this.labels = labels;
        this.emailLabels = emailLabels;
        this.emails = emails;
    }

    // PUBLIC_INTERFACE
    /** List labels for owner. */
    @Transactional(readOnly = true)
    public Page<LabelDto> list(User owner, Pageable pageable) {
        return labels.findByOwnerOrderByNameAsc(owner, pageable)
                .map(this::toDto);
    }

    // PUBLIC_INTERFACE
    /** Create a label for the owner. */
    @Transactional
    public Optional<LabelDto> create(User owner, CreateLabelRequest req) {
        String name = req.getName().trim();
        if (name.isBlank()) return Optional.empty();
        if (labels.existsByOwnerAndNameIgnoreCase(owner, name)) {
            return Optional.empty();
        }
        Label l = new Label(owner, name, sanitizeColor(req.getColor()));
        l = labels.save(l);
        return Optional.of(toDto(l));
    }

    // PUBLIC_INTERFACE
    /** Update a label if owned by user. */
    @Transactional
    public Optional<LabelDto> update(User owner, Long id, UpdateLabelRequest req) {
        return labels.findByIdAndOwner(id, owner).map(label -> {
            if (req.getName() != null && !req.getName().trim().isBlank()) {
                String newName = req.getName().trim();
                if (!label.getName().equalsIgnoreCase(newName)
                        && labels.existsByOwnerAndNameIgnoreCase(owner, newName)) {
                    // name conflict
                    return null;
                }
                label.setName(newName);
            }
            if (req.getColor() != null) {
                label.setColor(sanitizeColor(req.getColor()));
            }
            Label saved = labels.save(label);
            return toDto(saved);
        });
    }

    // PUBLIC_INTERFACE
    /** Delete a label and its assignments. */
    @Transactional
    public boolean delete(User owner, Long id) {
        return labels.findByIdAndOwner(id, owner).map(label -> {
            // delete all assignments for this label
            // JPA cascade isn't set; remove via repository (load and delete)
            // Simpler: find all emailIds then delete by id
            // Using repository delete method on managed label will cascade only if configured; do explicit cleanup
            List<EmailLabel> all = emailLabels.findAll().stream()
                    .filter(el -> el.getOwner().getId().equals(owner.getId()) && el.getLabel().getId().equals(id))
                    .toList();
            emailLabels.deleteAll(all);
            labels.delete(label);
            return true;
        }).orElse(false);
    }

    // PUBLIC_INTERFACE
    /** Assign a list of labels to an email (no-ops for already assigned). */
    @Transactional
    public boolean assignLabels(User owner, Long emailId, List<Long> labelIds) {
        Optional<Email> emailOpt = emails.findById(emailId)
                .filter(e -> e.getOwner().getId().equals(owner.getId()));
        if (emailOpt.isEmpty()) return false;
        Email email = emailOpt.get();

        for (Long lid : labelIds) {
            Label label = labels.findByIdAndOwner(lid, owner).orElse(null);
            if (label == null) continue;
            if (emailLabels.findByOwnerAndEmailAndLabel(owner, email, label).isEmpty()) {
                emailLabels.save(new EmailLabel(owner, email, label));
            }
        }
        return true;
    }

    // PUBLIC_INTERFACE
    /** Unassign a list of labels from an email (no-ops for not assigned). */
    @Transactional
    public boolean unassignLabels(User owner, Long emailId, List<Long> labelIds) {
        Optional<Email> emailOpt = emails.findById(emailId)
                .filter(e -> e.getOwner().getId().equals(owner.getId()));
        if (emailOpt.isEmpty()) return false;
        Email email = emailOpt.get();

        for (Long lid : labelIds) {
            Label label = labels.findByIdAndOwner(lid, owner).orElse(null);
            if (label == null) continue;
            emailLabels.findByOwnerAndEmailAndLabel(owner, email, label)
                    .ifPresent(emailLabels::delete);
        }
        return true;
    }

    // PUBLIC_INTERFACE
    /** List emails for the owner filtered by a given label. */
    @Transactional(readOnly = true)
    public Page<EmailListItem> listEmailsByLabel(User owner, Long labelId, Pageable pageable) {
        Label label = labels.findByIdAndOwner(labelId, owner).orElse(null);
        if (label == null) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        Page<Long> idsPage = emailLabels.findEmailIdsByOwnerAndLabel(owner, label, pageable);
        if (idsPage.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        // Fetch emails by ids, but keep ordering roughly by createdAt in join; repository returns unordered list
        List<Email> emailList = emails.findAllById(idsPage.getContent());
        // Map by id for quick lookup and sort by the id order in idsPage
        Map<Long, Email> map = new HashMap<>();
        for (Email e : emailList) map.put(e.getId(), e);
        List<Email> ordered = new ArrayList<>();
        for (Long id : idsPage.getContent()) {
            Email e = map.get(id);
            if (e != null) ordered.add(e);
        }
        return new PageImpl<>(ordered.stream().map(this::toListItem).toList(), pageable, idsPage.getTotalElements());
    }

    private String sanitizeColor(String color) {
        if (color == null) return null;
        String c = color.trim();
        if (c.isBlank()) return null;
        if (!c.startsWith("#")) c = "#" + c;
        if (c.length() > 10) c = c.substring(0, 10);
        return c;
    }

    private LabelDto toDto(Label l) {
        return new LabelDto(l.getId(), l.getName(), l.getColor(), l.getCreatedAt(), l.getUpdatedAt());
    }

    private EmailListItem toListItem(Email e) {
        String preview = e.getBodyText();
        if (preview == null || preview.isBlank()) {
            preview = e.getBodyHtml() != null ? e.getBodyHtml().replaceAll("<[^>]*>", "").trim() : "";
        }
        if (preview.length() > 140) {
            preview = preview.substring(0, 140) + "...";
        }
        return new EmailListItem(
                e.getId(),
                e.getFromAddr(),
                e.getToAddrs(),
                e.getSubject(),
                preview,
                e.isRead(),
                e.isStarred(),
                e.isArchived(),
                e.isDeleted(),
                e.getUpdatedAt(),
                e.getReceivedAt(),
                e.getSentAt()
        );
    }
}
