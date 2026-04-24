package com.uor.event_management_system.service.admin;

import com.uor.event_management_system.dto.EventRequestDto;
import com.uor.event_management_system.dto.EventResponseDto;
import com.uor.event_management_system.model.EventCategory;
import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.enums.EventStatus;
import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.repository.EventRepository;
import com.uor.event_management_system.service.EventRegistrationService;
import com.uor.event_management_system.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.chrono.ChronoLocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminEventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FileService filesService;

    @Autowired
    private EventRegistrationService eventRegistrationService;

    //used in table row date column
    private static final DateTimeFormatter TABLE_DATE = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    //used in detail page date
    private static final DateTimeFormatter DETAIL_PAGE_DATE = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy");

    //used in time display
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public List<EventEntity> searchEvents(String keyword) {
        return eventRepository.findByTitleContainingAndStatus(keyword,EventStatus.APPROVED);
    }


//    public List<EventEntity> getAllEvents() { //admin can see the all status(approved)
//        List<EventEntity> events = eventRepository.findByStatus(EventStatus.APPROVED);
//
//        for (EventEntity event : events) {
//            int count = filesService.getFileCount(event.getId());
//            int allRegisteredCount = eventRegistrationService.getRegisterEventCount(event.getId(), EventRegistrationStatus.APPROVED);
//            event.setFileCount(count);
//            event.setAllRegisteredCount(allRegisteredCount);
//            prepareEvent(event);
//
//        }
//        return events;
//    }

// Admin can see all statuses as Approved / pending / rejected
    public List<EventResponseDto> getAllEvents() {
        List<EventEntity> events = eventRepository.findAll();
        return events.stream().map(this::toDto).collect(Collectors.toList());
    }

//SEARCH EVENT
    public List<EventResponseDto> searchEvent(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllEvents();
        }
        //eventRepository.findByTitleContaining(keyword);
        List<EventEntity> events = eventRepository.findByTitleContainingAndStatus(keyword.trim(),EventStatus.APPROVED);
        return events.stream().map(this::toDto).collect(Collectors.toList());
    }

//FILTER BY CATEGORY

    public List<EventResponseDto> filterByCategory(EventCategory category) {
        if (category == null) return getAllEvents();
        return eventRepository.findAll().stream()
                .filter(eventEntity -> eventEntity.getEventCategory() == category)
                .map(this::toDto).collect(Collectors.toList());

    }
//GET BY ID (for admin detail page (eye icon))

public EventResponseDto getEventById(int id) {
        EventEntity event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("No event found with id " + id));
        return toDto(event);

}

//GET FOR EDIT (returns requestDTO to pre-fill the edit model)---Fetch event + convert it into editable form data (DTO) + send to frontend as JSON
    public EventRequestDto getEventForEdit(int id) {
        EventEntity event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("No event found with id " + id));
        return toRequestDto(event);
    }

//CREATE EVENT (saves new event as PENDING {needs admin approval})

    public void createEvent(EventRequestDto dto){
        EventEntity event = new EventEntity();
        copyDtoToEntity(dto, event);
        event.setStatus(EventStatus.PENDING);
        eventRepository.save(event);
    }

//UPDATE EVENT (keeps status choosing by admin in the form)

    public void updateEvent(int id, EventRequestDto dto){
        EventEntity event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("No event found with id " + id));
        copyDtoToEntity(dto, event);
        event.setStatus(dto.getStatus());
        eventRepository.save(event);
    }

//DELETE EVENT

public void deleteEvent(int id) {
        if(eventRepository.existsById(id)) {
            throw new RuntimeException("No event found with id " + id);
        }
        eventRepository.deleteById(id);
}

//TOTAL COUNT (7 total events in page header)

    public int getTotalCount() {
        return (int) eventRepository.count();
    }

    private void copyDtoToEntity(EventRequestDto dto, EventEntity event) {
        event.setTitle(dto.getTitle());
        event.setEventDescription(dto.getEventDescription());
        event.setEventLocation(dto.getEventLocation());

        if(dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate().toLocalDate());
        }

        event.setStartTime(LocalTime.from(dto.getStartTime()));
        event.setEndTime(LocalTime.from(dto.getEndTime()));
        event.setEventCategory(dto.getEventCategory());
        event.setSpots(dto.getMaxCapacity());
        event.setImage(dto.getImage());
        event.setStatus(EventStatus.PENDING);

    }


    public List<EventEntity> getByCategory(String category) {
        return eventRepository.findByEventCategoryAndStatus(category,EventStatus.APPROVED);
    }

    public int UpcomingEvents(){
        List<EventEntity> events =  eventRepository.findByStatus(EventStatus.APPROVED);
        LocalDateTime today = LocalDateTime.now();
        int count = 0;
        for(EventEntity event:events){
            if(event.getEventDate().isAfter(ChronoLocalDate.from(today))){
                count++;
            }
        }
        return count;
    }

//    public List<EventEntity> findByEventDateTimeAfter(LocalDateTime dateTime) {
//       return eventRepository.findByEventDateTimeAfterAndStatus(dateTime, EventStatus.APPROVED);
//   }
//
//    public List<EventEntity> findByEventDateTimeBefore(LocalDateTime dateTime) {
//        return eventRepository.findByEventDateTimeBeforeAndStatus(dateTime,EventStatus.APPROVED);
//    }



    public void prepareEvent(EventEntity event) {
        int total = event.getSpots();
        int registered = event.getAllRegisteredCount();
        int percent = 0;

        if (total > 0) {
            percent = (registered * 100) / total;
        }
        event.setPercentageStatus(String.valueOf(percent));

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

    private EventResponseDto toDto(EventEntity eventEntity) {

        EventResponseDto dto = new EventResponseDto();

        dto.setId(eventEntity.getId());
        dto.setTitle(eventEntity.getTitle());
        dto.setEventDescription(eventEntity.getEventDescription());
        dto.setFormattedDate(eventEntity.getEventDescription());
        dto.setEventLocation(eventEntity.getEventLocation());
        dto.setEventDate(eventEntity.getEventDate());

        if(eventEntity.getStartTime() != null) dto.setStartTime(eventEntity.getStartTime());
        if(eventEntity.getEndTime() != null) dto.setEndTime(eventEntity.getEndTime());

        dto.setEventCategory(eventEntity.getEventCategory());
        dto.setStatus(eventEntity.getStatus());
        dto.setImage(eventEntity.getImage());
        dto.setMaxCapacity(eventEntity.getSpots());

//        dto.setOrganizerName(eventEntity.getOrganizerName());
//        dto.setOrganizerEmail(eventEntity.getOrganizerEmail());

//        if(eventEntity.getTags() != null && !eventEntity.getTags().isEmpty()) {
//            dto.setTags(Arrays.asList(eventEntity.getTags().split(",")));
//        }else {
//           dto.setTags(Collections.emptyList());
//       }

        int fileCount = filesService.getFileCount(eventEntity.getId());
        dto.setFileCount(fileCount);

        int registeredCount = eventRegistrationService.getRegisterEventCount(eventEntity.getId(), EventRegistrationStatus.APPROVED);
        dto.setAllRegisteredCount(registeredCount);

        int max = eventEntity.getSpots();
        int left = Math.max(0,max - registeredCount);
        int percent = max > 0 ? (registeredCount * 100)/ max : 0;

        dto.setSpotsLeft(left);
        dto.setRegistrationPercent(percent);
        dto.setNearlyFull(left >= 0 && left <= 10 && max > 0);
        dto.setPercentageStatus(left <= 0 ? "Full" : left + " spots left");

        if(left <= 0 || left <= 10) dto.setProgressBarColor("red");
        else if (left >= 75) dto.setProgressBarColor("yellow");
        else dto.setProgressBarColor("green");

        if(eventEntity.getEventDate() != null) {
            dto.setFormattedDate(eventEntity.getEventDate().format(TABLE_DATE));
        }

        if(eventEntity.getStartTime() != null) {
            StringBuilder t = new StringBuilder(eventEntity.getStartTime().format(TIME_FORMAT));
            dto.setFormattedTime(t.toString());
        }

        if(eventEntity.getEventCategory() != null) {
            String category = eventEntity.getEventCategory().getCategory();
            dto.setCategoryDisplay(category.charAt(0) + category.substring(1).toLowerCase());

        }

        if(eventEntity.getStatus() != null) {
            String status = eventEntity.getStatus().name();
            dto.setStatusDisplay(status.charAt(0) + status.substring(1).toLowerCase());
        }

        return dto;
    }

    private EventRequestDto toRequestDto(EventEntity eventEntity) {
        EventRequestDto dto = new EventRequestDto();
        dto.setId(eventEntity.getId());
        dto.setTitle(eventEntity.getTitle());
        dto.setEventDescription(eventEntity.getEventDescription());
        dto.setFullDescription(eventEntity.getEventDescription());
        dto.setEventLocation(eventEntity.getEventLocation());
        dto.setEventDate(LocalDateTime.from(eventEntity.getStartTime()));
        dto.setStartTime(LocalDateTime.from(eventEntity.getStartTime()));
        dto.setEndTime(LocalDateTime.from(eventEntity.getEndTime()));
        dto.setEventCategory(eventEntity.getEventCategory());
        dto.setMaxCapacity(eventEntity.getSpots());
        dto.setStatus(eventEntity.getStatus());
        dto.setImage(eventEntity.getImage());
//        dto.setOrganizerName(eventEntity.getOrganizerName());
//        dto.setOrganizerEmail(eventEntity.getOrganizerEmail());
//        dto.setTags(eventEntity.getTags());
        return dto;


    }
}
