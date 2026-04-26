package com.uor.event_management_system.service.admin;

import com.uor.event_management_system.dto.DashboardDto;
import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.enums.EventStatus;
import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.model.EventRegistration;
import com.uor.event_management_system.repository.EventRegistrationRep;
import com.uor.event_management_system.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventRegistrationRep eventRegistrationRep;

    // Date formatter for upcoming events panel: "May 2, 2026"
    private static final DateTimeFormatter PANEL_DATE =
            DateTimeFormatter.ofPattern("MMM d, yyyy");

    // ─────────────────────────────────────────────────────────────
    //  Main method — builds the entire dashboard payload
    // ─────────────────────────────────────────────────────────────
    public DashboardDto buildDashboard() {

        DashboardDto dto = new DashboardDto();

        List<EventEntity> allEvents      = eventRepository.findAll();
        List<EventEntity> approvedEvents = eventRepository.findByStatus(EventStatus.APPROVED);
        LocalDateTime now = LocalDateTime.now();

        // ── Stat Cards ───────────────────────────────────────────────

        dto.setTotalEvents(allEvents.size());

        long upcoming = approvedEvents.stream()
                .filter(e -> toDateTime(e).isAfter(now))
                .count();
        dto.setUpcomingEvents((int) upcoming);

        List<EventRegistration> allRegistrations = eventRegistrationRep.findAll();

        int totalRegistrations = (int) allRegistrations.stream()
                .filter(r -> r.getStatus() == EventRegistrationStatus.APPROVED)
                .count();
        dto.setTotalRegistrations(totalRegistrations);

        int attended = (int) allRegistrations.stream()
                .filter(r -> r.getStatus() == EventRegistrationStatus.APPROVED)
                .filter(r -> toEndDateTime(r.getEvent()).isBefore(now))
                .count();
        dto.setTotalAttended(attended);

        int rate = totalRegistrations > 0
                ? (attended * 100) / totalRegistrations
                : 0;
        dto.setAttendanceRate(rate);

        dto.setEmailsSent(0);

        // ── Upcoming Events Panel ────────────────────────────────────

        List<EventEntity> upcomingList = approvedEvents.stream()
                .filter(e -> toDateTime(e).isAfter(now))
                .sorted(Comparator.comparing(EventEntity::getEventDate)
                        .thenComparing(EventEntity::getStartTime))
                .limit(3)
                .collect(Collectors.toList());

        dto.setUpcomingEventList(toUpcomingDtos(upcomingList));

        // ── Events by Category ───────────────────────────────────────

        Map<String, Integer> categoryMap = buildCategoryMap(allEvents);
        dto.setEventsByCategory(categoryMap);

        // Compute max in Java — never in Thymeleaf SpEL
        int maxCat = categoryMap.values().stream()
                .max(Integer::compareTo)
                .orElse(1);
        dto.setMaxCategoryCount(maxCat);

        // ── Recent Registrations ─────────────────────────────────────

        dto.setRecentRegistrations(buildRecentRegistrations(allRegistrations, now));

        return dto;
    }

    // ─────────────────────────────────────────────────────────────
    //  Upcoming events panel helpers
    // ─────────────────────────────────────────────────────────────

    private List<DashboardDto.UpcomingEventDto> toUpcomingDtos(List<EventEntity> events) {

        // Cycle through a small set of icons — extend as needed
        String[] icons = {
                "bi-trophy-fill",
                "bi-palette-fill",
                "bi-mortarboard-fill",
                "bi-cpu-fill",
                "bi-briefcase-fill",
                "bi-music-note-beamed"
        };

        List<DashboardDto.UpcomingEventDto> list = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {

            EventEntity event = events.get(i);
            DashboardDto.UpcomingEventDto upDto = new DashboardDto.UpcomingEventDto();

            upDto.setId(event.getId());
            upDto.setTitle(event.getTitle());
            upDto.setFormattedDate(event.getEventDate().format(PANEL_DATE));
            upDto.setSpots(event.getSpots());

            // Count approved registrations for this specific event
            int regCount = eventRegistrationRep
                    .countByEvent_IdAndStatus(event.getId(), EventRegistrationStatus.APPROVED);
            upDto.setRegistered(regCount);

            // Progress bar width
            double percent = event.getSpots() > 0
                    ? (regCount * 100.0) / event.getSpots()
                    : 0;
            upDto.setProgressPercent(Math.min(percent, 100));

            // Cycle icon
            upDto.setIconClass(icons[i % icons.length]);

            list.add(upDto);
        }

        return list;
    }

    // ─────────────────────────────────────────────────────────────
    //  Category map helper
    // ─────────────────────────────────────────────────────────────

    private Map<String, Integer> buildCategoryMap(List<EventEntity> events) {

        // LinkedHashMap preserves insertion order for the template
        Map<String, Integer> map = new LinkedHashMap<>();

        for (EventEntity event : events) {
            if (event.getEventCategory() != null) {
                String cat = event.getEventCategory().getCategory();
                map.merge(cat, 1, Integer::sum);
            }
        }

        // Sort descending by count so the largest bar comes first
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    // ─────────────────────────────────────────────────────────────
    //  Recent registrations helper
    // ─────────────────────────────────────────────────────────────

    private List<DashboardDto.RecentRegistrationDto> buildRecentRegistrations(
            List<EventRegistration> registrations,
            LocalDateTime now) {

        String[] avatarClasses = {"c1", "c2", "c3", "c4"};

        return registrations.stream()
                .filter(r -> r.getDate_time() != null)
                .sorted(Comparator.comparing(EventRegistration::getDate_time).reversed())
                .limit(4)
                .map(r -> {
                    DashboardDto.RecentRegistrationDto rDto =
                            new DashboardDto.RecentRegistrationDto();

                    // Initials from first + last name
                    String first = r.getUser().getFirstName();
                    String last  = r.getUser().getLastName();
                    String initials = String.valueOf((safeChar(first) + safeChar(last))).toUpperCase();
                    rDto.setInitials(initials);

                    rDto.setFullName(first + " " + last);
                    rDto.setEventTitle(r.getEvent().getTitle());
                    rDto.setTimeAgo(timeAgo(r.getDate_time(), now));

                    // Cycle avatar colour class
                    int idx = registrations.indexOf(r) % avatarClasses.length;
                    rDto.setAvatarClass(avatarClasses[Math.abs(idx)]);

                    return rDto;
                })
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────
    //  Utility helpers
    // ─────────────────────────────────────────────────────────────

    /** Start-time as LocalDateTime */
    private LocalDateTime toDateTime(EventEntity e) {
        return LocalDateTime.of(e.getEventDate(), e.getStartTime());
    }

    /** End-time as LocalDateTime */
    private LocalDateTime toEndDateTime(EventEntity e) {
        return LocalDateTime.of(e.getEventDate(), e.getEndTime());
    }

    /** Safe first character of a string */
    private char safeChar(String s) {
        return (s != null && !s.isEmpty()) ? s.charAt(0) : '?';
    }

    /** Human-readable "X ago" label */
    private String timeAgo(LocalDateTime time, LocalDateTime now) {

        long minutes = ChronoUnit.MINUTES.between(time, now);
        if (minutes < 1)  return "just now";
        if (minutes < 60) return minutes + "m ago";

        long hours = ChronoUnit.HOURS.between(time, now);
        if (hours < 24)   return hours + "h ago";

        long days = ChronoUnit.DAYS.between(time, now);
        return days + "d ago";
    }


}