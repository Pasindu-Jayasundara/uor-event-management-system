package com.uor.event_management_system.model;

import com.uor.event_management_system.enums.EventStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name="event")
public class EventEntity {




        @Id
        private int id;

        @Column(name = "name")
        private String title;

        @Column(name = "description")
        private String eventDescription;

        @Column(name = "location")
        private String eventLocation;

        @Column(name = "date_time")
        private LocalDateTime eventDateTime;

        @Column(name = "event_category_id")
        private int eventCategory;

        @Column(name = "banner")
        private String image;

        private int spots;

        @Enumerated(EnumType.STRING)
        private EventStatus status;


        @Transient
        private int FileCount;

        @Transient
        private int allRegisteredCount;


        @Transient
        private int percent;
        @Transient
        private String PercentageStatus;

}