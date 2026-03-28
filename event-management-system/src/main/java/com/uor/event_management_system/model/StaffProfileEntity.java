package com.uor.event_management_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "staff_profile")

@Getter @Setter
public class StaffProfileEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "verified", columnDefinition = "TINYINT")
    private int verified;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
