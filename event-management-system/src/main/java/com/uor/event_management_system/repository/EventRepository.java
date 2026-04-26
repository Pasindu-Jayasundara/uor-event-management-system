package com.uor.event_management_system.repository;

import com.uor.event_management_system.enums.EventStatus;
import com.uor.event_management_system.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Integer> {
    List<EventEntity> findByTitleContainingAndStatus(String keyword, EventStatus status);

    List<EventEntity> findByEventCategory_IdAndStatus(int eventCategoryId, EventStatus status);




    List<EventEntity> findByStatus(EventStatus status);
    int countByStatus(EventStatus status);

    List<EventEntity> findByEventCategoryAndStatus(String category, EventStatus eventStatus);

//    List<EventEntity> findByEventDateTimeBeforeAndStatus(LocalDateTime dateTime, EventStatus eventStatus);
}
