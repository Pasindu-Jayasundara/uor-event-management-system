package com.uor.event_management_system.repository;

import com.uor.event_management_system.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Integer> {
    List<EventEntity> findByTitleContainingIgnoreCase(String keyword);
    List <EventEntity> findByEventCategory(String eventCategory);
}
