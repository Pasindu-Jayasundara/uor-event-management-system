package com.uor.event_management_system.service;

import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.model.EventRegistration;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.EventRegistrationRep;
import com.uor.event_management_system.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventRegistrationService {


    @Autowired
    EventRegistrationRep eventRegistrationRep;
    @Autowired
    private EventRepository eventRepository;

    public int getRegisterEventCount(int eventId, EventRegistrationStatus status) {
        return eventRegistrationRep.countByEvent_IdAndStatus(eventId, status);

    }

    @Transactional
    public void cancelRegistration(int userId, int eventId) {
        eventRegistrationRep.deleteByUser_IdAndEvent_Id(userId, eventId);
    }

    public Map<Integer, String> getUserRegistrationStatus(UserEntity user) {

        Map<Integer, String> map = new HashMap<>();
        List<EventRegistration> registrations = eventRegistrationRep.findByUser_id(user.getId());

        for (EventRegistration reg : registrations) {

            map.put(reg.getEvent().getId(), reg.getStatus().name());

        }

        return map;


    }


    public String register(int userId,int eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        EventRegistration reg = new EventRegistration();
        if (event.getSpots() == 0) {
            reg.setStatus(EventRegistrationStatus.WAITLIST);
        } else {
            reg.setStatus(EventRegistrationStatus.APPROVED);
            event.setSpots(event.getSpots() - 1);
        }
        return  null;
    }
    @Transactional
    public void approve(int registerEventId) {
        EventRegistration reg = eventRegistrationRep.findById(registerEventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        EventEntity event = reg.getEvent();
        if (event.getSpots() == 0) {
            reg.setStatus(EventRegistrationStatus.WAITLIST);
            eventRegistrationRep.save(reg);
        }

        reg.setStatus(EventRegistrationStatus.APPROVED);
        event.setSpots(event.getSpots() - 1);
        eventRepository.save(event);
        eventRegistrationRep.save(reg);

    }

    public void reject(int registerEventId) {
        EventRegistration reg = eventRegistrationRep.findById(registerEventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        reg.setStatus(EventRegistrationStatus.REJECTED);
        eventRegistrationRep.save(reg);
    }













}
