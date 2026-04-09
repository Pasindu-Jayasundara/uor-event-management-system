package com.uor.event_management_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
        private String eventTime;

        @Column(name = "event_category_id")
        private int eventCategory;

        @Column(name = "banner")
        private String image;





}
