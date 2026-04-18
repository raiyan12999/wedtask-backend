package com.wedding.wedtask.service;


import com.wedding.wedtask.dto.Dtos;
import com.wedding.wedtask.model.Task;
import com.wedding.wedtask.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Dtos.TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(Dtos.TaskResponse::from)
                .collect(Collectors.toList());
    }

    public List<Dtos.TaskResponse> getTasksByUser(String userName) {
        return taskRepository.findByAssignedToIgnoreCase(userName)
                .stream()
                .map(Dtos.TaskResponse::from)
                .collect(Collectors.toList());
    }

    public Dtos.TaskResponse createTask(Dtos.CreateTaskRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required");
        }
        if (request.getTimeCategory() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time category is required");
        }

        Task task = new Task();
        task.setTitle(request.getTitle().trim());
        // Fix: null-safe description — avoid NPE if description is omitted
        task.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        task.setAssignedTo(request.getAssignedTo() != null ? request.getAssignedTo().trim() : null);
        task.setTimeCategory(request.getTimeCategory());
        task.setStatus(Task.TaskStatus.PENDING);
        task.setUrgent(request.isUrgent());
        task.setScheduledAt(request.getScheduledAt());

        return Dtos.TaskResponse.from(taskRepository.save(task));
    }

    public Dtos.TaskResponse updateTask(UUID taskId, Dtos.UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            task.setTitle(request.getTitle().trim());
        }
        if (request.getDescription() != null) {
            // Fix: trim description on update as well
            task.setDescription(request.getDescription().trim());
        }
        if (request.getAssignedTo() != null) {
            task.setAssignedTo(request.getAssignedTo().trim());
        }
        if (request.getTimeCategory() != null) {
            task.setTimeCategory(request.getTimeCategory());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getIsUrgent() != null) {
            task.setUrgent(request.getIsUrgent());
        }
        if (request.getScheduledAt() != null) {
            task.setScheduledAt(request.getScheduledAt());
        }

        return Dtos.TaskResponse.from(taskRepository.save(task));
    }

    public Dtos.ProgressResponse getProgress() {
        long total = taskRepository.count();
        long completed = taskRepository.countByStatus(Task.TaskStatus.DONE);
        long pending = total - completed;

        Dtos.ProgressResponse response = new Dtos.ProgressResponse();
        response.setTotalTasks(total);
        response.setCompletedTasks(completed);
        response.setPendingTasks(pending);
        response.setPercentage(total == 0 ? 0 : (completed * 100.0 / total));
        return response;
    }

    public void deleteTask(UUID taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        taskRepository.deleteById(taskId);
    }
}