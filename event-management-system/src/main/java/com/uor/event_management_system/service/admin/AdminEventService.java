package com.uor.event_management_system.service.admin;

import com.uor.event_management_system.dto.EventRequestDto;
import com.uor.event_management_system.dto.EventResponseDto;
import com.uor.event_management_system.enums.EventStep;
import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.repository.EventRepository;
import com.uor.event_management_system.service.EventRegistrationService;
import com.uor.event_management_system.service.FileService;

import com.uor.event_management_system.validator.BasicEventHandler;
import com.uor.event_management_system.validator.EventHandler;
import com.uor.event_management_system.validator.EventHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminEventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FileService filesService;

    @Autowired
    private EventRegistrationService eventRegistrationService;

    @Autowired
    private EventHandlerFactory factory;

    public EventResponseDto addEvent(EventRequestDto eventRequestDto, EventStep step) {

        //Chain execution
        EventHandler handler = factory.getChain(step);
        handler.handle(eventRequestDto);

        //Step 02: Only SAVE at final step
        if(step == EventStep.FILES){
            EventEntity eventEntity = new EventEntity();

            eventEntity.setTitle(eventRequestDto.getTitle());
            eventEntity.setEventDescription(eventRequestDto.getEventDescription());
            eventEntity.setEventLocation(eventRequestDto.getEventLocation());
            eventEntity.setEventDate(eventRequestDto.getEventDate());
            eventEntity.setStartTime(eventRequestDto.getStartTime());
            eventEntity.setEndTime(eventRequestDto.getEndTime());
            eventEntity.setEventCategory(eventRequestDto.getEventCategory());
            eventEntity.setSpots(eventRequestDto.getSpots());
            eventEntity.setStatus(eventRequestDto.getStatus());
            eventEntity.setImage(eventRequestDto.getImage());

            eventRepository.save(eventEntity);
            return mapToResponse(eventEntity);
        }

        return new EventResponseDto();
    }

    //Convert EventEntity to Response DTo
    private EventResponseDto mapToResponse(EventEntity event) {

        EventResponseDto res = new EventResponseDto();

        res.setId(event.getId());
        res.setTitle(event.getTitle());
        res.setEventDescription(event.getEventDescription());
        res.setEventLocation(event.getEventLocation());
        res.setEventCategory(event.getEventCategory());
        res.setStatus(event.getStatus());

        res.setEventDate(event.getEventDate());
        res.setStartTime(event.getStartTime());
        res.setEndTime(event.getEndTime());

        res.setImage(event.getImage());
        res.setSpots(event.getSpots());

        return res;
    }

    public List<EventResponseDto> getAllEvents() {
        List<EventEntity> events = eventRepository.findAll();
        return events.stream().map(this::mapToResponse).toList();
    }
}
