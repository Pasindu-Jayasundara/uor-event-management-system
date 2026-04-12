package com.uor.event_management_system.dto;

import com.uor.event_management_system.model.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlatformUserDTO {

    private int id;
    private String name;
    private String email;
    private String role;
    private String dept;
    private int enabled;

    public static PlatformUserDTO from(UserEntity u) {

        // StaffProfileEntity has no department — only a verified flag
        // UndergraduateProfileEntity has a DepartmentEntity relation
        String dept = null;
        if (u.getUndergraduateProfile() != null
                && u.getUndergraduateProfile().getDepartment() != null) {
            dept = u.getUndergraduateProfile().getDepartment().getDepartment(); // adjust if getter differs
        }

        return PlatformUserDTO.builder()
                .id(u.getId())
                .name(u.getFirstName() + " " + u.getLastName())
                .email(u.getEmail())
                .role(u.getRole().getRole().toLowerCase())  // e.g. "admin", "staff", "student"
                .dept(dept)
                .enabled(u.getEnabled())
                .build();
    }
}