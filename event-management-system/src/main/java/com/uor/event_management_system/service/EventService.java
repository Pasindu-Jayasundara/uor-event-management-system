package com.uor.event_management_system.service;

import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository unieventsRepo;

    public List<EventEntity> searchEvents(String keyword) {
        return unieventsRepo.findByTitleContainingIgnoreCase(keyword);
    }


    public List<EventEntity> getallEvents() {
        return unieventsRepo.findAll();
    }


    public List<EventEntity> getByCategory(String category) {
        return unieventsRepo.findByEventCategory(category);
    }












}
