package com.wedding.wedtask.service;


import com.wedding.wedtask.dto.Dtos;
import com.wedding.wedtask.model.User;
import com.wedding.wedtask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Value("${app.admin-code}")
    private String adminCode;

    public List<Dtos.UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(Dtos.UserResponse::from)
                .collect(Collectors.toList());
    }

    public List<Dtos.UserResponse> getFreeUsers() {
        return userRepository.findByStatus(User.UserStatus.FREE)
                .stream()
                .map(Dtos.UserResponse::from)
                .collect(Collectors.toList());
    }

    public Dtos.UserResponse createOrGetUser(Dtos.CreateUserRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }

        String trimmedName = request.getName().trim();
        boolean wantsAdmin = request.getAdminCode() != null
                && request.getAdminCode().equals(adminCode);

        // Return existing user, upgrading to admin if code provided
        return userRepository.findByNameIgnoreCase(trimmedName).map(existing -> {
            if (wantsAdmin && !existing.isAdmin()) {
                existing.setAdmin(true);
                userRepository.save(existing);
            }
            return Dtos.UserResponse.from(existing);
        }).orElseGet(() -> {
            User user = new User();
            user.setName(trimmedName);
            user.setStatus(User.UserStatus.FREE);
            user.setAdmin(wantsAdmin);
            return Dtos.UserResponse.from(userRepository.save(user));
        });
    }

    public Dtos.UserResponse updateStatus(UUID userId, Dtos.UpdateUserStatusRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setStatus(request.getStatus());
        return Dtos.UserResponse.from(userRepository.save(user));
    }

    public boolean verifyAdminCode(String code) {
        return adminCode.equals(code);
    }
}
