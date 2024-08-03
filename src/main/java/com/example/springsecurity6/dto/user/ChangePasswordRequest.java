package com.example.springsecurity6.dto.user;

import com.example.springsecurity6.controller.user.validator.ValidPassword;
import lombok.Builder;

@Builder
public record ChangePasswordRequest(
        @ValidPassword
        String currentPassword,
        @ValidPassword
        String newPassword,
        @ValidPassword
        String confirmationPassword
) {
}
