package com.uor.event_management_system.validator;

import com.uor.event_management_system.dto.EventRequestDto;

public interface EventHandler {
    void setNext(EventHandler next);
    void handle(EventRequestDto eventRequestDto);
}
