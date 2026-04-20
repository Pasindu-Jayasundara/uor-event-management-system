package com.uor.event_management_system.dto;

import com.uor.event_management_system.enums.EventCategory;
import com.uor.event_management_system.enums.EventStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

//used for the create and edit form

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    private int id;

    @NotBlank(message = "Event title is requires.")
    @Size(max = 100, message = "Title must not exceed 100 characters.")
    private String title;

    @NotBlank(message = "Event description is required")
    private String eventDescription;

    @NotBlank(message = "Event location is required.")
    private String eventLocation;

    @NotNull(message = "Event date is required.")
    @Future(message = "Event date must be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime eventDate;

    @NotNull(message = "Start time is required.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalDateTime endTime;

    @NotNull(message = "Event category is required.")
    private EventCategory eventCategory;

    private String image;

    @NotNull(message = "Status is required.")
    private EventStatus status;

    @NotNull(message = "Max capacity is required.")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 500, message = "Capacity can not be exceed 500")
    private int maxCapacity;


}
