package com.uor.event_management_system.validator;

import com.uor.event_management_system.enums.EventStep;
import org.springframework.stereotype.Component;

@Component
public class EventHandlerFactory {
    private final BasicEventHandler basic;
    private final DetailsEventHandler details;
    private final FilesEventHandler files;

    public EventHandlerFactory(BasicEventHandler basic, DetailsEventHandler details, FilesEventHandler files) {
        this.basic = basic;
        this.details = details;
        this.files = files;
    }

    public EventHandler getChain(EventStep step) {
        switch (step) {
            case BASIC:
                return basic;
            case DETAILS:
                basic.setNext(details);
                return details;
            case FILES:
                basic.setNext(details);
                details.setNext(files);
                return files;
            default:
                throw new RuntimeException("Invalid step");
        }
    }
}
