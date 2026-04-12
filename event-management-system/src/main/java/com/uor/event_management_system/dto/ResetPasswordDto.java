package com.uor.event_management_system.dto;

import com.uor.event_management_system.annotation.ValidPasswordAnnotation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {

    @ValidPasswordAnnotation
    private String password;

    @ValidPasswordAnnotation
    private String confirmPassword;
}