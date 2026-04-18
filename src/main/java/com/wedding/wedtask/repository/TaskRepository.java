package com.wedding.wedtask.repository;


import com.wedding.wedtask.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByAssignedTo(String assignedTo);
    List<Task> findByTimeCategory(Task.TimeCategory timeCategory);
    List<Task> findByStatus(Task.TaskStatus status);
    List<Task> findByAssignedToIgnoreCase(String assignedTo);
    long countByStatus(Task.TaskStatus status);
}
