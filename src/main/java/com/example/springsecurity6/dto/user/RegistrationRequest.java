package com.example.springsecurity6.dto.user;

import com.example.springsecurity6.controller.user.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegistrationRequest(
        @NotBlank(message = "Firstname is mandatory")
        @NotEmpty(message = "Firstname is mandatory")
        String firstname,
        @NotBlank(message = "Lastname is mandatory")
        @NotEmpty(message = "Lastname is mandatory")
        String lastname,
        @NotBlank(message = "Email is mandatory")
        @NotEmpty(message = "Email is mandatory")
        @Email(message = "Email format is wrong")
        String email,
        @NotBlank(message = "Password is mandatory")
        @NotEmpty(message = "Password is mandatory")
        @Size(min = 4,message = "Min size is 4")
        @ValidPassword
        String password
) {
}
