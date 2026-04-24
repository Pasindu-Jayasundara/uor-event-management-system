package com.uor.event_management_system.repository;

import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRegistrationRep extends JpaRepository<EventRegistration, Integer> {


    int countByUser_IdAndStatus(Integer userId, EventRegistrationStatus status);
    int countByEvent_IdAndStatus(Integer eventId, EventRegistrationStatus status);
   // boolean existsByUser_IdAndEvent_IdAndStatus(int userId, int eventId, EventRegistrationStatus status);


    Optional<EventRegistration> findByUser_IdAndEvent_Id(int userId, int eventId);
    void deleteByUser_IdAndEvent_Id(int userId, int eventId);
   List<EventRegistration> findByUser_id(Integer user_id);

   //shashini
   boolean existsByUser_IdAndEvent_IdAndStatusNot(
           int userId, int eventId, EventRegistrationStatus status);


    Optional<EventRegistration> findFirstByEvent_IdAndStatusOrderByRegisteredAtAsc(
            int eventId, EventRegistrationStatus status);

    List<EventRegistration>findByEvent_Id(int eventId);

    List<EventRegistration>findByEvent_IdAndStatus(int eventId, EventRegistrationStatus status);
}
