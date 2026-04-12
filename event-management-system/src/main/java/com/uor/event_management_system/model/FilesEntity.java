package com.uor.event_management_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="files")
public class FilesEntity {

    @Id
    private int id;

    @Column(name="date_time")
    private LocalDateTime FileDateTime;

    private String url;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;








}
