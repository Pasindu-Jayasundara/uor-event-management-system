package com.uor.event_management_system.model;

import com.uor.event_management_system.enums.EventStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name="event")
public class EventEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)  //Auto incremented
        private int id;

        @Column(name = "name" , nullable = false)
        private String title;

        @Column(name = "description")
        private String eventDescription;

        @Column(name = "location", nullable = false)
        private String eventLocation;

        @Column(name = "date", nullable = false)
        private LocalDate eventDate;

        @Column(name = "start_time",nullable = false)
        private LocalTime startTime;

        @Column(name="end_time", nullable = false)
        private LocalTime endTime;

        @ManyToOne
        @JoinColumn(name = "event_category_id")
        private EventCategory eventCategory;

        @Column(name = "banner")
        private String image;

        @Column(nullable = false)
        private int spots;

        @Column(name = "request_approval")
        private boolean requestApproval;

        @Column(name = "has_limit")
        private boolean hasLimit;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private EventStatus status;


        @Transient
        private int fileCount;

        @Transient
        private int allRegisteredCount;

        @Transient
        private int registrationPercent;

        @Transient
        private String percentageStatus;



}
