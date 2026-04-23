package com.uor.event_management_system.service;

import com.uor.event_management_system.model.EventCategory;
import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.repository.EventCategoryRepository;
import com.uor.event_management_system.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventCategoryService {

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    public List<EventCategory> getAllCategories(){
        return eventCategoryRepository.findAll();
    }
}
