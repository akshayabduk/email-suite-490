package com.example.demo.label;

import com.example.demo.email.Email;
import com.example.demo.user.User;
import jakarta.persistence.*;

import java.time.Instant;

/**
 * PUBLIC_INTERFACE
 * Join entity between Email and Label, scoped by owner.
 * Ensures an email can have multiple labels and labels can be applied to many emails.
 */
@Entity
@Table(name = "email_labels",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_email_label", columnNames = {"owner_id", "email_id", "label_id"})
        },
        indexes = {
                @Index(name = "idx_email_label_owner", columnList = "owner_id"),
                @Index(name = "idx_email_label_label", columnList = "label_id"),
                @Index(name = "idx_email_label_email", columnList = "email_id")
        })
public class EmailLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Owner to reinforce user scoping (denormalized for unique constraint simplicity)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_email_label_owner"))
    private User owner;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id", nullable = false, foreignKey = @ForeignKey(name = "fk_email_label_email"))
    private Email email;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id", nullable = false, foreignKey = @ForeignKey(name = "fk_email_label_label"))
    private Label label;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
    }

    public EmailLabel() {}

    public EmailLabel(User owner, Email email, Label label) {
        this.owner = owner;
        this.email = email;
        this.label = label;
    }

    // PUBLIC_INTERFACE
    public Long getId() { return id; }
    // PUBLIC_INTERFACE
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    // PUBLIC_INTERFACE
    public Email getEmail() { return email; }
    public void setEmail(Email email) { this.email = email; }
    // PUBLIC_INTERFACE
    public Label getLabel() { return label; }
    public void setLabel(Label label) { this.label = label; }
    // PUBLIC_INTERFACE
    public Instant getCreatedAt() { return createdAt; }
}
