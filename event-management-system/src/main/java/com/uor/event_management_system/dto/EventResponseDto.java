package com.uor.event_management_system.dto;

import com.uor.event_management_system.enums.EventCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

//Used to table and grid view

@Getter
@Setter
@NoArgsConstructor
public class EventResponseDto {
    private int id;
    private String title;
    private String description;
    private String location;
    private EventCategory eventCategory;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;



}
