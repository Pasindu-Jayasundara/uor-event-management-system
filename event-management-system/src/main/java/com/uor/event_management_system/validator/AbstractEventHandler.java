package com.uor.event_management_system.validator;

import com.uor.event_management_system.dto.EventRequestDto;

public abstract class AbstractEventHandler implements EventHandler {
    protected EventHandler next;
    @Override
    public void setNext(EventHandler next) {
        this.next = next;
    }

    protected void next(EventRequestDto eventRequestDto) {
        if (next != null) {
            next.handle(eventRequestDto);
        }
    }
}
