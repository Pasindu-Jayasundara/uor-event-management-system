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


    public String register(int userId,int eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean alreadyRegistered = eventRegistrationRep
                .existsByUser_IdAndEvent_IdAndStatusNot(
                        userId, eventId, EventRegistrationStatus.CANCELLED);
        if (alreadyRegistered) {
            return "Already registered";
        }


        EventRegistration reg = new EventRegistration();
        reg.setUser(user);
        reg.setEvent(event);

        EventRegistrationStatus status = reg.getStatus();

        if(status == EventRegistrationStatus.APPROVED && event.isHasLimit()){
            event.setSpots(event.getSpots() - 1);
            eventRepository.save(event);
        }
        eventRegistrationRep.save(reg);
        return status.name();
    }

    private EventRegistrationStatus determineStatus(EventEntity event) {
        boolean hasLimit = event.isHasLimit();
        boolean needsApproval = event.isRequestApproval();
        int spots = event.getSpots();

        if(!hasLimit && !needsApproval){
            return EventRegistrationStatus.REGISTERED;
        }
        if(!hasLimit && needsApproval){
            return EventRegistrationStatus.PENDING;
        }
        if(!hasLimit && spots > 0){
            if(!needsApproval){
                return EventRegistrationStatus.REGISTERED;
            }
            else {
                return EventRegistrationStatus.PENDING;
            }
        }
        return EventRegistrationStatus.WAITLIST;
    }

    @Transactional
    public String approve(int registrationId) {
        EventRegistration reg = eventRegistrationRep.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        if(reg.getStatus() != EventRegistrationStatus.PENDING){
            return "Registration already approved"+ reg.getStatus();
        }

        EventEntity event = reg.getEvent();

        if(!event.isHasLimit()){
            reg.setStatus(EventRegistrationStatus.REGISTERED);
        } else if (event.getSpots() > 0) {
            reg.setStatus(EventRegistrationStatus.REGISTERED);
            event.setSpots(event.getSpots() - 1);
            eventRepository.save(event);
        }
        else {
            reg.setStatus(EventRegistrationStatus.WAITLIST);
        }
        eventRegistrationRep.save(reg);
        return reg.getStatus().name();
    }

    //Reject event
    @Transactional
    public String reject(int registrationId) {
        EventRegistration reg = eventRegistrationRep.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        if(reg.getStatus() != EventRegistrationStatus.REJECTED){
            return "Registration already rejected"+ reg.getStatus();
        }

        if(reg.getStatus() == EventRegistrationStatus.REGISTERED && reg.getEvent().isHasLimit()){
            EventEntity event = reg.getEvent();
            event.setSpots(event.getSpots() + 1);
            eventRepository.save(event);
        }
        reg.setStatus(EventRegistrationStatus.REJECTED);
        eventRegistrationRep.save(reg);
        return reg.getStatus().name();
    }

    //Cancel registration
    @Transactional
    public String cancel(int userId,int eventId) {
        EventRegistration reg = eventRegistrationRep.findByUser_IdAndEvent_Id(userId,eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if(reg.getStatus() == EventRegistrationStatus.REGISTERED && reg.getEvent().isHasLimit()){
            EventEntity event = reg.getEvent();
            event.setSpots(event.getSpots() + 1);
            eventRepository.save(event);
        }
        eventRegistrationRep.delete(reg);
        return "CANCELLED";

    }

    //Auto promote from waitelist
    @Transactional
    public void promoteFromWaitlist(EventEntity event) {
        if (event.getSpots() <= 0) return;

        eventRegistrationRep.findFirstByEvent_IdAndStatusOrderByRegisteredAtAsc(event.getId(), EventRegistrationStatus.WAITLIST).ifPresent(waitlistedReg -> {
            if (event.isRequestApproval()) {
                waitlistedReg.setStatus(EventRegistrationStatus.PENDING);
            } else {
                waitlistedReg.setStatus(EventRegistrationStatus.REGISTERED);
                event.setSpots(event.getSpots() - 1);
                eventRepository.save(event);
            }
            eventRegistrationRep.save(waitlistedReg);

        });
    }

        public int getRegistrationCount(int eventId, EventRegistrationStatus status) {
            return eventRegistrationRep.countByEvent_IdAndStatus(eventId, status);

        }

        public Map<Integer,String>getUserRegistrations(UserEntity user) {
            Map<Integer,String> userRegistrations = new HashMap<>();
            List<EventRegistration>registrations = eventRegistrationRep.findByUser_id(user.getId());

            for(EventRegistration reg : registrations){
                userRegistrations.put(reg.getEvent().getId(),reg.getStatus().name());
            }
            return userRegistrations;
        }

        public List<EventRegistration>getEventRegistrations(int eventId) {
            return eventRegistrationRep.findByEvent_Id(eventId);
        }

        public List<EventRegistration>getByEvent_IdAndStatus(int eventId, EventRegistrationStatus status) {
            return eventRegistrationRep.findByEvent_IdAndStatus(eventId, status);
        }
    }

















