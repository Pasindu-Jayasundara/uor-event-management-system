package com.uor.event_management_system.service;

import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.repository.EventRegistrationRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventRegistrationService {


    @Autowired
    EventRegistrationRep  eventRegistrationRep;

    public int getRegisterEventCount(int eventId , EventRegistrationStatus status) {
        return eventRegistrationRep.countByEvent_IdAndStatus(eventId, status);

    }













}
