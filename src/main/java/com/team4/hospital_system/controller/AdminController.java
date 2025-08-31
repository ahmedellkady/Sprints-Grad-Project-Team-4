package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.response.UserDto;
import com.team4.hospital_system.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> listUsers(@RequestParam(required = false) String role) {
        List<UserDto> users = adminService.listUsers(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable long userId) {
        UserDto user = adminService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{adminId}/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long adminId, @PathVariable long userId) {
        adminService.deleteUser(adminId, userId);
        return ResponseEntity.noContent().build();
    }
}
