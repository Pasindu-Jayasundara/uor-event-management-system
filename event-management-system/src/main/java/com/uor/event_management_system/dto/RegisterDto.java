package com.uor.event_management_system.dto;

import com.uor.event_management_system.annotation.ValidPasswordAnnotation;
import com.uor.event_management_system.enums.AccountType;
import com.uor.event_management_system.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {

    @NotBlank(message = "First name is required")
    @Size(max = 45, message = "First name must be under 45 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 45, message = "Last name must be under 45 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(
            message = "Please provide a valid University email (@*.ruh.ac.lk)",
            regexp = "^[a-zA-Z0-9._%+-]+@[a-z0-9.]+\\.ruh\\.ac\\.lk$"
    )
    @Size(max = 255,message = "Email too Long")
    private String email;

    @NotBlank(message = "Password is required")
    @ValidPasswordAnnotation
    private String password;

    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;

    @NotNull(message = "Enable criteria not fount")
    private int enabled;

    @NotBlank(message = "Role is required")
    private String role = UserRole.ROLE_USER.name();

    @NotBlank(message = "Account type is required")
    private String accountType = AccountType.PROFILE_UNDERGRADUATE.name();

}
