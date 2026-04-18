package com.wedding.wedtask.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.wedding.wedtask.model.Task;
import com.wedding.wedtask.model.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

public class Dtos {

    // ── User DTOs ──────────────────────────────────────────────

    @Data
    public static class CreateUserRequest {
        private String name;
        private String adminCode; // optional; if provided and correct → isAdmin = true
    }

    @Data
    public static class UpdateUserStatusRequest {
        private User.UserStatus status;
    }

    @Data
    public static class UserResponse {
        private UUID id;
        private String name;
        private User.UserStatus status;
        // Fix: must annotate here too — this DTO field also has the boolean naming issue.
        @JsonProperty("isAdmin")
        private boolean isAdmin;
        private LocalDateTime createdAt;

        public static UserResponse from(User user) {
            UserResponse r = new UserResponse();
            r.id = user.getId();
            r.name = user.getName();
            r.status = user.getStatus();
            r.isAdmin = user.isAdmin();
            r.createdAt = user.getCreatedAt();
            return r;
        }
    }

    // ── Task DTOs ──────────────────────────────────────────────

    @Data
    public static class CreateTaskRequest {
        private String title;
        private String description;
        private String assignedTo;
        private Task.TimeCategory timeCategory;
        // Fix: Jackson needs this to correctly deserialize "isUrgent" from JSON body.
        @JsonProperty("isUrgent")
        private boolean isUrgent;
        private LocalDateTime scheduledAt;
    }

    @Data
    public static class UpdateTaskRequest {
        private String title;
        private String description;
        private String assignedTo;
        private Task.TimeCategory timeCategory;
        private Task.TaskStatus status;
        // Fix: same deserialization fix for PATCH body.
        @JsonProperty("isUrgent")
        private Boolean isUrgent;
        private LocalDateTime scheduledAt;
    }

    @Data
    public static class TaskResponse {
        private UUID id;
        private String title;
        private String description;
        private String assignedTo;
        private Task.TimeCategory timeCategory;
        private Task.TaskStatus status;
        // Fix: ensure the JSON response key is "isUrgent" not "urgent".
        @JsonProperty("isUrgent")
        private boolean isUrgent;
        private LocalDateTime scheduledAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static TaskResponse from(Task task) {
            TaskResponse r = new TaskResponse();
            r.id = task.getId();
            r.title = task.getTitle();
            r.description = task.getDescription();
            r.assignedTo = task.getAssignedTo();
            r.timeCategory = task.getTimeCategory();
            r.status = task.getStatus();
            r.isUrgent = task.isUrgent();
            r.scheduledAt = task.getScheduledAt();
            r.createdAt = task.getCreatedAt();
            r.updatedAt = task.getUpdatedAt();
            return r;
        }
    }

    // ── Progress DTO ──────────────────────────────────────────

    @Data
    public static class ProgressResponse {
        private long totalTasks;
        private long completedTasks;
        private long pendingTasks;
        private double percentage;
    }

    // ── Admin verify ──────────────────────────────────────────

    @Data
    public static class VerifyAdminRequest {
        private String adminCode;
    }

    @Data
    public static class VerifyAdminResponse {
        private boolean valid;
    }
}
