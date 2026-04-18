package com.wedding.wedtask.controller;


import com.wedding.wedtask.dto.Dtos;
import com.wedding.wedtask.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<List<Dtos.TaskResponse>> getTasks(
            @RequestParam(required = false) String assignedTo) {
        if (assignedTo != null && !assignedTo.isBlank()) {
            return ResponseEntity.ok(taskService.getTasksByUser(assignedTo));
        }
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping("/tasks")
    public ResponseEntity<Dtos.TaskResponse> createTask(
            @RequestBody Dtos.CreateTaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @PatchMapping("/tasks/{id}")
    public ResponseEntity<Dtos.TaskResponse> updateTask(
            @PathVariable UUID id,
            @RequestBody Dtos.UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks/progress")
    public ResponseEntity<Dtos.ProgressResponse> getProgress() {
        return ResponseEntity.ok(taskService.getProgress());
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
