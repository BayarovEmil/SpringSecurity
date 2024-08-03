package com.example.springsecurity6.controller.user;

import com.example.springsecurity6.repository.UserRepository;
import com.example.springsecurity6.service.user.AdminService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
@Tag(name = "Admin Controller")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Hidden
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/getAllUsersInformation")
    public ResponseEntity<?> getAllUsersInformation() {
        return ResponseEntity.ok(adminService.getAllUsersInformation());
    }

    @PatchMapping("/assignRoleToUser/{user-id}")
    public ResponseEntity<?> assignRoleToUser(
            @PathVariable("user-id") Integer id
    ) {
        return adminService.assignRoleToUser(id);
    }

    @DeleteMapping("/deleteAccount/{user-id}")
    public ResponseEntity<?> deleteAccount(
            @PathVariable("user-id") Integer id
    ) {
        adminService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }
}
