package com.uor.event_management_system.model;

import com.uor.event_management_system.enums.EventCategory;
import com.uor.event_management_system.enums.EventStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data

@Table(name="event")
public class EventEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)  //Auto incremented
        private int id;

        @Column(name = "name" , nullable = false)
        private String title;

        @Column(name = "description", columnDefinition = "TEXT")
        private String eventDescription;

        @Column(name = "location", nullable = false)
        private String eventLocation;

        @Column(name = "date", nullable = false)
        private LocalDateTime eventDate;

        @Column(name = "start_time",nullable = false)
        private LocalDateTime startTime;

        @Column(name="end_time", nullable = false)
        private LocalDateTime endTime;

        @Enumerated(EnumType.STRING)
        @Column(name = "event_category_id", nullable = false)
        private EventCategory eventCategory;

        @Column(name = "banner")
        private String image;

        @Column(name = "max_capacity", nullable = false)
        private int maxCapacity;

        @Enumerated(EnumType.STRING)
        @Column(name="status", nullable = false)
        private EventStatus status;


        @Transient
        private int fileCount;

        @Transient
        private int allRegisteredCount;

        @Transient
        private int registrationPercent;

        @Transient
        private String percentageStatus; //capacity status

}
