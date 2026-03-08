package com.uor.event_management_system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterDto {

    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String nic;
    private String role = "ROLE_USER";
}
