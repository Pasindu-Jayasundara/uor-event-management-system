package com.uor.event_management_system.service;

import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.enums.EventStatus;
import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository unieventsRepo;

    @Autowired
    FileService filesService;

    @Autowired
    EventRegistrationService  eventRegistrationService;

    public List<EventEntity> searchEvents(String keyword) {
        return unieventsRepo.findByTitleContainingAndStatus(keyword,EventStatus.APPROVED);
    }


    public List<EventEntity> getallEvents() { //get the all event details with the filecount

        List<EventEntity> events = unieventsRepo.findByStatus(EventStatus.APPROVED);

        for (EventEntity event : events) {
            int count = filesService.getFileCount(event.getId());
            int allRegisteredCount = eventRegistrationService.getRegisterEventCount(event.getId(), EventRegistrationStatus.APPROVED);
            event.setFileCount(count);
            event.setAllRegisteredCount(allRegisteredCount);
            prepareEvent(event);


        }
        return events;
    }


    public List<EventEntity> getByCategory(int category) {
        return unieventsRepo.findByEventCategory_IdAndStatus(category,EventStatus.APPROVED);
    }

    public int UpcomingEvents(){

        List<EventEntity> events =  unieventsRepo.findByStatus(EventStatus.APPROVED);
        LocalDateTime today = LocalDateTime.now();
        int count = 0;
        for(EventEntity event:events){

            LocalDateTime eventDateTime = LocalDateTime.of(
                    event.getEventDate(),
                    event.getStartTime()
            );
            if(eventDateTime.isAfter(today)){

                count++;



            }

        }
        return count;
    }

    public List<EventEntity> getUpcomingEvents() {

        List<EventEntity> events =  unieventsRepo.findByStatus(EventStatus.APPROVED);
        LocalDateTime now = LocalDateTime.now();
        List<EventEntity> upcomingEvents = new ArrayList<EventEntity>();

        for(EventEntity event:events){

            LocalDateTime eventDateTime = LocalDateTime.of(
                    event.getEventDate(),
                    event.getStartTime()
            );
            if(eventDateTime.isAfter(now)){

                upcomingEvents.add(event);
            }



        }
        return upcomingEvents;

    }

    public List<EventEntity> getPastEvents() {

        List<EventEntity> events =  unieventsRepo.findByStatus(EventStatus.APPROVED);
        LocalDateTime now = LocalDateTime.now();
        List<EventEntity> pastEvents = new ArrayList<>();
        for(EventEntity event:events){
            LocalDateTime eventDateTime = LocalDateTime.of(
                    event.getEventDate(),
                    event.getEndTime()
            );
            if(eventDateTime.isBefore(now)){
                pastEvents.add(event);
            }

        }
        return pastEvents;






    }



    public void prepareEvent(EventEntity event) {

        int total = event.getSpots();
        int registered = event.getAllRegisteredCount();

        int percent = 0;

        if (total > 0) {
            percent = (registered * 100) / total;
        }

        event.setRegistrationPercent(percent);


        if (percent == 100) {
            event.setPercentageStatus("full");
        } else if (percent > 70) {
            event.setPercentageStatus("high");
        } else if (percent > 40) {
            event.setPercentageStatus("medium");
        } else {
            event.setPercentageStatus("low");
        }
    }














}