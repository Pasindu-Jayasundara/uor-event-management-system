package com.uor.event_management_system.validator;

import com.uor.event_management_system.dto.EventRequestDto;
import org.springframework.stereotype.Component;

@Component
public class DetailsEventHandler extends AbstractEventHandler{

    @Override
    public void handle(EventRequestDto dto) {
        if (dto.getEventDescription() == null || dto.getEventDescription().isBlank()) {
            throw new RuntimeException("Description required");
        }

        if (dto.getOrganizerIds() == null || dto.getOrganizerIds().isEmpty()) {
            throw new RuntimeException("Organizer required");
        }

        System.out.println("✅ DETAILS passed");

        next(dto);
    }
}
