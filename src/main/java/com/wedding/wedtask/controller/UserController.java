package com.wedding.wedtask.controller;


import com.wedding.wedtask.dto.Dtos;
import com.wedding.wedtask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<Dtos.UserResponse>> getAllUsers(
            @RequestParam(required = false) String status) {
        if ("FREE".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(userService.getFreeUsers());
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<Dtos.UserResponse> createOrGetUser(
            @RequestBody Dtos.CreateUserRequest request) {
        return ResponseEntity.ok(userService.createOrGetUser(request));
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<Dtos.UserResponse> updateUserStatus(
            @PathVariable UUID id,
            @RequestBody Dtos.UpdateUserStatusRequest request) {
        return ResponseEntity.ok(userService.updateStatus(id, request));
    }

    @PostMapping("/admin/verify")
    public ResponseEntity<Dtos.VerifyAdminResponse> verifyAdmin(
            @RequestBody Dtos.VerifyAdminRequest request) {
        Dtos.VerifyAdminResponse response = new Dtos.VerifyAdminResponse();
        response.setValid(userService.verifyAdminCode(request.getAdminCode()));
        return ResponseEntity.ok(response);
    }
}
