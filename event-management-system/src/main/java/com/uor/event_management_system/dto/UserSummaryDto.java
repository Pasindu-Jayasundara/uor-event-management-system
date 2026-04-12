package com.uor.event_management_system.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UserSummaryDto {

    private int totalUsers;
    private int totalActiveUsers;
    private int totalInactiveUsers;
}
