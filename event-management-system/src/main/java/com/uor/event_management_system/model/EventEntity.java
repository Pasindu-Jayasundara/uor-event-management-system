package com.uor.event_management_system.model;

import com.uor.event_management_system.enums.EventCategory;
import com.uor.event_management_system.enums.EventStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data

@Table(name="event")
public class EventEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)  //Auto incremented
        private int id;



        @Column(name = "title" , nullable = false)
        private String title;

        @Column(name = "short_description", columnDefinition = "TEXT")
        private String eventDescription;

        @Column(name = "full_description", columnDefinition = "TEXT")
        private String fullDescription;

        @Column(name = "location", nullable = false)
        private String eventLocation;



        @Column(name = "date", nullable = false)
        private LocalDate eventDate;

        @Column(name = "start_time",nullable = false)
        private LocalDateTime startTime;

        @Column(name="end_time", nullable = false)
        private LocalDateTime endTime;



        @Enumerated(EnumType.STRING)
        @Column(name = "event_category_id", nullable = false)
        private EventCategory eventCategory;

        @Enumerated(EnumType.STRING)
        @Column(name="status", nullable = false)
        private EventStatus status;



        @Column(name = "banner")
        private String image;


        @Column(name = "max_capacity", nullable = false)
        private int maxCapacity;

        //Organizer details in details button
        @Column(name = "organizer_name")
        private String organizerName;

        @Column(name = "organizer_email")
        private String organizerEmail;

        // Comma-separated tags "AI,Blockchain,Cloud"
        @Column(name = "tags")
        private String tags;



        @Transient
        private int fileCount;

        @Transient
        private int allRegisteredCount;

        @Transient
        private int registrationPercent;  //(allRegisteredCount*100)/maxCapacity

        @Transient
        private String percentageStatus; //capacity status(53 spots left || "FULL")

        @Transient
        private int spotsLeft;            // maxCapacity - allRegisteredCount

        @Transient
        private boolean nearlyFull;     // true when spotLeft <=10

}
