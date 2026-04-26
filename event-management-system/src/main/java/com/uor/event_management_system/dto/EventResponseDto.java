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


    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String image;
    private int fileCount;

    private int allRegisteredCount;
    private int spots;
    private int spotsLeft;
    private int registrationPercent;
    private String percentageStatus; //capacity status(53 spots left || "FULL")
    private boolean nearlyFull;






}
