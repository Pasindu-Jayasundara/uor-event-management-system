package com.uor.event_management_system.validator;

import com.uor.event_management_system.dto.EventRequestDto;
import org.springframework.stereotype.Component;

@Component
public class FilesEventHandler extends AbstractEventHandler  {
    @Override
    public void handle(EventRequestDto dto) {
        if (dto.getImage() == null || dto.getImage().isBlank()) {
            throw new RuntimeException("Image required");
        }

        System.out.println("FILES passed");

        next(dto);
    }
}
