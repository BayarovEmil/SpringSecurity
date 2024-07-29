package com.example.springsecurity6.controller.user;

import com.example.springsecurity6.dto.user.ChangePasswordRequest;
import com.example.springsecurity6.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication connectedUser
    ) {
        userService.changePassword(request,connectedUser);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(
            Authentication connectedUser
    ) {
        userService.forgetPassword(connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(
            Authentication connectedUser,
            @RequestParam String code,
            @RequestParam  String newPassword,
            @RequestParam String confirmationPassword
    ) {
        userService.resetPassword(connectedUser,code,newPassword,confirmationPassword);
        return ResponseEntity.ok().build();
    }
}
