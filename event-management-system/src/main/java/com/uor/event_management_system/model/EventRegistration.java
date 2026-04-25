package com.uor.event_management_system.model;


import com.uor.event_management_system.enums.EventRegistrationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="event_registration")
public class EventRegistration {

    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "event_id",nullable = false)
    private EventEntity event;


    private LocalDateTime date_time;

    @Enumerated(EnumType.STRING)
    private EventRegistrationStatus status;


    @Transient
    private String filterTag;


}
