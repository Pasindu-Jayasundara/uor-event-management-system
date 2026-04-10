package com.uor.event_management_system.service;

import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository unieventsRepo;

    @Autowired
    FileService filesService;

    public List<EventEntity> searchEvents(String keyword) {
        return unieventsRepo.findByTitleContainingIgnoreCase(keyword);
    }


    public List<EventEntity> getallEvents() { //get the all event details with the filecount

        List<EventEntity> events = unieventsRepo.findAll();

        for (EventEntity event : events) {
            int count = filesService.getFileCount(event.getId());
            event.setFileCount(count);
        }
        return events;
    }


    public List<EventEntity> getByCategory(String category) {
        return unieventsRepo.findByEventCategory(category);
    }

    public int UpcomingEvents(){

        List<EventEntity> events =  unieventsRepo.findAll();
        LocalDateTime today = LocalDateTime.now();
        int count = 0;
        for(EventEntity event:events){
            if(event.getEventDateTime().isAfter(today)){

                count++;



            }

        }
        return count;
    }

    public List<EventEntity> findByEventDateTimeAfter(LocalDateTime dateTime) {
        return unieventsRepo.findByEventDateTimeAfter(dateTime);
    }

    public List<EventEntity> findByEventDateTimeBefore(LocalDateTime dateTime) {
        return unieventsRepo.findByEventDateTimeBefore(dateTime);
    }












}
