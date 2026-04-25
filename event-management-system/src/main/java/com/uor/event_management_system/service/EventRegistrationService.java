package com.uor.event_management_system.service;

import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.model.EventRegistration;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.EventRegistrationRep;
import com.uor.event_management_system.repository.EventRepository;
import com.uor.event_management_system.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.cfg.MapperBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventRegistrationService {


    @Autowired
    EventRegistrationRep eventRegistrationRep;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MapperBuilder mapperBuilder;

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

}


















