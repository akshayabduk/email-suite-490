package com.example.demo.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * PUBLIC_INTERFACE
 * DTO used for composing and sending/saving a draft email.
 */
public class ComposeEmailRequest {

    @Schema(description = "Comma-separated to addresses", example = "recipient@example.com")
    private String to;

    @Schema(description = "Comma-separated cc addresses", example = "cc1@example.com,cc2@example.com")
    private String cc;

    @Schema(description = "Comma-separated bcc addresses", example = "bcc1@example.com")
    private String bcc;

    @Schema(description = "Subject", example = "Hello")
    @Size(max = 500)
    private String subject;

    @Schema(description = "Body in HTML")
    private String bodyHtml;

    @Schema(description = "Body in plain text")
    private String bodyText;

    @Schema(description = "Thread id if replying/forwarding", example = "thread-abc-123")
    private String threadId;

    public ComposeEmailRequest() {}

    public ComposeEmailRequest(String to, String cc, String bcc, String subject, String bodyHtml, String bodyText, String threadId) {
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.bodyHtml = bodyHtml;
        this.bodyText = bodyText;
        this.threadId = threadId;
    }

    // PUBLIC_INTERFACE
    public String getTo() { return to; }
    // PUBLIC_INTERFACE
    public String getCc() { return cc; }
    // PUBLIC_INTERFACE
    public String getBcc() { return bcc; }
    // PUBLIC_INTERFACE
    public String getSubject() { return subject; }
    // PUBLIC_INTERFACE
    public String getBodyHtml() { return bodyHtml; }
    // PUBLIC_INTERFACE
    public String getBodyText() { return bodyText; }
    // PUBLIC_INTERFACE
    public String getThreadId() { return threadId; }

    public void setTo(String to) { this.to = to; }
    public void setCc(String cc) { this.cc = cc; }
    public void setBcc(String bcc) { this.bcc = bcc; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setBodyHtml(String bodyHtml) { this.bodyHtml = bodyHtml; }
    public void setBodyText(String bodyText) { this.bodyText = bodyText; }
    public void setThreadId(String threadId) { this.threadId = threadId; }
}
