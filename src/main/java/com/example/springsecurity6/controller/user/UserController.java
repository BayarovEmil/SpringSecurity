package com.example.springsecurity6.controller.user;

import com.example.springsecurity6.controller.user.validator.ValidPassword;
import com.example.springsecurity6.dto.user.ChangePasswordRequest;
import com.example.springsecurity6.model.User;
import com.example.springsecurity6.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/password/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication connectedUser
    ) {
        userService.changePassword(request,connectedUser);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password/forgetPassword")
    public ResponseEntity<?> forgetPassword(
            Authentication connectedUser
    ) {
        userService.forgetPassword(connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/password/resetPassword")
    public ResponseEntity<?> resetPassword(
            Authentication connectedUser,
            @RequestParam String code,
            @RequestParam @ValidPassword String newPassword,
            @RequestParam String confirmationPassword
    ) {
        userService.resetPassword(connectedUser,code,newPassword,confirmationPassword);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivatedAccount")
    public ResponseEntity<?> deactivateAccount(
            Authentication connectedUser
    ) {
        userService.deactivateAccount(connectedUser);
        return ResponseEntity.ok().build();
    }

}
