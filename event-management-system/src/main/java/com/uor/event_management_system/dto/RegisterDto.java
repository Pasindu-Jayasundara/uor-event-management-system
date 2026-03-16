package com.uor.event_management_system.dto;

import com.uor.event_management_system.model.DepartmentEntity;
import com.uor.event_management_system.model.FacultyEntity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterDto {

    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private FacultyEntity faculty;
    private String role = "ROLE_USER";
    private int studyYear;
    private DepartmentEntity department;
}
