package com.uor.event_management_system.service;

import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.model.EventRegistration;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.EventRegistrationRep;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventRegistrationService {


    @Autowired
    EventRegistrationRep  eventRegistrationRep;

    public int getRegisterEventCount(int eventId , EventRegistrationStatus status) {
        return eventRegistrationRep.countByEvent_IdAndStatus(eventId, status);

    }

    @Transactional
    public void cancelRegistration(int userId, int eventId) {
        eventRegistrationRep.deleteByUser_IdAndEvent_Id(userId, eventId);
    }

    public Map<Integer,String> getUserRegistrationStatus(UserEntity user) {

        Map<Integer,String> map = new HashMap<>();
        List <EventRegistration> registrations = eventRegistrationRep.findByUser_id(user.getId());

        for(EventRegistration reg : registrations){

            map.put(reg.getEvent().getId(),reg.getStatus().name());

        }

        return map;
    }

    public void register(UserEntity user, EventEntity event) {

        // ❗ prevent duplicate registration
        Optional<EventRegistration> existing =
                eventRegistrationRep.findByUser_IdAndEvent_Id(user.getId(), event.getId());

        if (existing.isPresent()) {
            return; // already registered
        }

        int approvedCount =
                eventRegistrationRep.countByEvent_IdAndStatus(
                        event.getId(), EventRegistrationStatus.APPROVED
                );

        EventRegistrationStatus status;

        if (event.getHasLimit()  && approvedCount >= event.getSpots()) {
            status = EventRegistrationStatus.WAITLIST;
        } else if (event.getRequestApproval()) {
            status = EventRegistrationStatus.PENDING;
        } else {
            status = EventRegistrationStatus.APPROVED;
        }

        EventRegistration reg = new EventRegistration();
        reg.setUser(user);
        reg.setEvent(event);
        reg.setStatus(status);
        reg.setDate_time(LocalDateTime.now());

        eventRegistrationRep.save(reg);
    }


    public List<EventRegistration> getUserRegistrations(UserEntity user) {
        return eventRegistrationRep.findByUser_id(user.getId());
    }

    public Map<String, Integer> getRegistrationStats(int userId) {

        List<EventRegistration> list = eventRegistrationRep.findByUser_id(userId);

        int total = list.size();
        int upcoming = 0;
        int attended = 0;
        int rejected = 0;

        LocalDateTime now = LocalDateTime.now();



        for (EventRegistration reg : list) {

            LocalDateTime eventDateTime = LocalDateTime.of(reg.getEvent().getEventDate(),
                    reg.getEvent().getStartTime());

            if (reg.getStatus() == EventRegistrationStatus.REJECTED) {
                rejected++;
            }
            else if (eventDateTime.isAfter(now)) {
                upcoming++;
            }
            else {
                attended++;
            }
        }

        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("upcoming", upcoming);
        stats.put("attended", attended);
        stats.put("cancelled", rejected);

        return stats;
    }
















}
