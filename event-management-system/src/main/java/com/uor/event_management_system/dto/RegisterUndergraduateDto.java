package com.uor.event_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUndergraduateDto {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Faculty ID is required")
    private String facultyId;

    @NotBlank(message = "Study year is required")
    private String studyYear;

    @NotBlank(message = "Department is required")
    private String department;
}
