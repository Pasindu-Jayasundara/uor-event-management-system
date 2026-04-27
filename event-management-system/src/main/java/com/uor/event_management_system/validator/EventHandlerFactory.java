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

        BasicEventHandler basicHandler = new BasicEventHandler();
        DetailsEventHandler detailsHandler = new DetailsEventHandler();
        FilesEventHandler filesHandler = new FilesEventHandler();

        switch (step) {
            case BASIC:
                return basicHandler;

            case DETAILS:
                basicHandler.setNext(detailsHandler);
                return basicHandler;

            case FILES:
                basicHandler.setNext(detailsHandler);
                detailsHandler.setNext(filesHandler);
                return basicHandler;

            default:
                throw new RuntimeException("Invalid step");
        }
    }

}
