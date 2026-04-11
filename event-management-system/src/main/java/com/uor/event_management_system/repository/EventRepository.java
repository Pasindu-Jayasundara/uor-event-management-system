package com.uor.event_management_system.repository;

import com.uor.event_management_system.enums.EventStatus;
import com.uor.event_management_system.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {
    List<EventEntity> findByTitleContainingIgnoreCase(String keyword);
    List <EventEntity> findByEventCategory(String eventCategory);

    List <EventEntity> findByEventDateTimeAfter(LocalDateTime dateTime);
    List <EventEntity> findByEventDateTimeBefore(LocalDateTime dateTime);
    List<EventEntity> findByStatus(EventStatus status);

}
