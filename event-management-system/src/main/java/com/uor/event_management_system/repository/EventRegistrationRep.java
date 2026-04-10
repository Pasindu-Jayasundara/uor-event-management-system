package com.uor.event_management_system.repository;

import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRegistrationRep extends JpaRepository<EventRegistration, Integer> {


    int countByUser_IdAndStatus(Integer userId, EventRegistrationStatus status);



}
