package com.uor.event_management_system.dto;

import com.uor.event_management_system.model.AccountTypeEntity;
import com.uor.event_management_system.model.DepartmentEntity;
import com.uor.event_management_system.model.FacultyEntity;
import com.uor.event_management_system.model.StudyYearEntity;
import com.uor.event_management_system.util.AccountType;
import com.uor.event_management_system.util.UserRole;
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
    private String facultyId;
    private String role = UserRole.ROLE_USER.name();
    private String studyYear;
    private String department;
    private String accountType = AccountType.PROFILE_UNDERGRADUATE.name();
}
