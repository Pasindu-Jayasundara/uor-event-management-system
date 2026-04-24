package com.uor.event_management_system.dto;

import com.uor.event_management_system.enums.EventStatus;
import com.uor.event_management_system.model.EventCategory;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

//Used to table and grid view

@Getter
@Setter
@NoArgsConstructor
public class EventResponseDto {
    private int id;


    private String title;
    private String eventDescription;
    private String fullDescription;
    private String eventLocation;

    private EventCategory eventCategory;
    private EventStatus status;

    // Display strings (already formatted for Thymeleaf th:text)
    private String categoryDisplay;         // "Technology"
    private String statusDisplay;           // "Published"

    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String image;
    private int fileCount;

    private int allRegisteredCount;
    private int maxCapacity;
    private int spotsLeft;
    private int registrationPercent;
    private String percentageStatus; //capacity status(53 spots left || "FULL")
    private boolean nearlyFull;

    private String formattedDate;           // "Sun, Mar 15, 2026"
    private String formattedTime;           // "09:00 – 17:00"

// ── Extra fields (for detail page) ───────────────────────────

    private String organizerName;
    private String organizerEmail;
    private List<String> tags;              // ["AI","Blockchain","Cloud"]
    private List<String> participantNames;  // from EventRegistration module

// ── Grid-specific ─────────────────────────────────────────────

    private boolean registeredByCurrentUser; // shows "✓ Registered" badge
    private String progressBarColor;         // "red", "yellow", "green"



}
