package com.uor.event_management_system.dto;

import com.uor.event_management_system.enums.EmailTemplateType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class EmailDto {

    @NotBlank(message = "Email is required")
    @Email(
            message = "Please provide a valid University email (@*.ruh.ac.lk)",
            regexp = "^[a-zA-Z0-9._%+-]+@[a-z0-9.]+\\.ruh\\.ac\\.lk$",
            flags = Pattern.Flag.CASE_INSENSITIVE
    )
    private String to;
    private String subject;
    private EmailTemplateType emailTemplateType;
    private Map<String, Object> variables;
}
