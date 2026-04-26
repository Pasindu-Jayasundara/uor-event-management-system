package com.uor.event_management_system.validator;

import com.uor.event_management_system.dto.EventRequestDto;
import org.springframework.stereotype.Component;

@Component
public class BasicEventHandler extends AbstractEventHandler {
    @Override
    public void handle(EventRequestDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new RuntimeException("Title is required");
        }

        if (dto.getEventLocation() == null || dto.getEventLocation().isBlank()) {
            throw new RuntimeException("Location is required");
        }

        if (dto.getEventDate() == null) {
            throw new RuntimeException("Date is required");
        }

        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new RuntimeException("Start & end time required");
        }

        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new RuntimeException("End time must be after start time");
        }

        if (dto.getSpots() <= 0) {
            throw new RuntimeException("Capacity must be > 0");
        }

        System.out.println("BASIC passed");

        next(dto);
    }

}
