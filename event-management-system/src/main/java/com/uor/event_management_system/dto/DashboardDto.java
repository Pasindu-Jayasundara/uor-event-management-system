package com.uor.event_management_system.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class DashboardDto {

    // Stat Cards
    private int totalEvents;
    private int upcomingEvents;
    private int totalRegistrations;
    private int totalAttended;
    private int emailsSent;
    private int attendanceRate;

    // Category panel
    private Map<String, Integer> eventsByCategory;
    private int maxCategoryCount; // ← ADD THIS

    // Panels
    private List<UpcomingEventDto> upcomingEventList;
    private List<RecentRegistrationDto> recentRegistrations;

    // ── Nested DTOs ──────────────────────────────────────────────

    @Getter
    @Setter
    public static class UpcomingEventDto {
        private int id;
        private String title;
        private String formattedDate;
        private int registered;
        private int spots;
        private double progressPercent;
        private String iconClass;
    }

    @Getter
    @Setter
    public static class RecentRegistrationDto {
        private String initials;
        private String fullName;
        private String eventTitle;
        private String timeAgo;
        private String avatarClass;
    }
}