package com.example.demo.label;

import com.example.demo.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

/**
 * PUBLIC_INTERFACE
 * JPA Entity representing a user-defined label (like Gmail labels).
 * Each label is owned by a single user and can be assigned to many emails.
 */
@Entity
@Table(name = "labels",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_label_owner_name", columnNames = {"owner_id", "name"})
        },
        indexes = {
                @Index(name = "idx_label_owner", columnList = "owner_id")
        })
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_label_owner"))
    private User owner;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    @Schema(description = "Label name (unique per user)", example = "Work")
    private String name;

    @Size(max = 10)
    @Schema(description = "Hex color like #2563EB", example = "#2563EB")
    @Column(length = 10)
    private String color;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Label() {}

    public Label(User owner, String name, String color) {
        this.owner = owner;
        this.name = name;
        this.color = color;
    }

    // PUBLIC_INTERFACE
    /** Label id. */
    public Long getId() { return id; }
    // PUBLIC_INTERFACE
    /** Owning user of the label. */
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    // PUBLIC_INTERFACE
    /** Label name. */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    // PUBLIC_INTERFACE
    /** Label color hex. */
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    // PUBLIC_INTERFACE
    public Instant getCreatedAt() { return createdAt; }
    // PUBLIC_INTERFACE
    public Instant getUpdatedAt() { return updatedAt; }
}
