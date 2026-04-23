package com.uor.event_management_system.repository;

import com.uor.event_management_system.enums.EventStatus;
import com.uor.event_management_system.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {

    //Used by search bar
    List<EventEntity> findByTitleContainingAndStatus(String keyword, EventStatus status);

    List <EventEntity> findByEventCategoryAndStatus(String eventCategory, EventStatus status);


    List<EventEntity> findByEventDateTimeAfterAndStatus(LocalDate eventDate, LocalTime startTime, EventStatus status);

    List<EventEntity> findByEventDateTimeBeforeAndStatus(LocalDateTime dateTime, EventStatus status);
    List<EventEntity> findByStatus(EventStatus status);
    int countByStatus(EventStatus status);


//  Search bar — searches title by keyword (case-insensitive)
//    List<EventEntity> findByTitleContainingIgnoreCase(String keyword);
//
//    // Filter icon — filter by category badge
//    List<EventEntity> findByEventCategory(EventCategory category);
//
//    // Optional — filter by status (Published / Draft)
//    List<EventEntity> findByStatus(EventStatus status);
//
//    // Optional — combined search + filter
//    List<EventEntity> findByTitleContainingIgnoreCaseAndEventCategory(
//            String keyword, EventCategory category);
}
