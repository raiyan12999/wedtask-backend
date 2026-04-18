package com.wedding.wedtask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private String assignedTo; // person name

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeCategory timeCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.PENDING;

    // Fix: @JsonProperty forces Jackson to use "isUrgent" as the JSON key.
    // Without this, Lombok generates isUrgent() which Jackson maps to "urgent".
    @JsonProperty("isUrgent")
    @Column(nullable = false)
    private boolean isUrgent = false;

    @Column
    private LocalDateTime scheduledAt; // optional scheduled time

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum TimeCategory {
        BEFORE, DURING, AFTER
    }

    public enum TaskStatus {
        PENDING, DONE
    }
}