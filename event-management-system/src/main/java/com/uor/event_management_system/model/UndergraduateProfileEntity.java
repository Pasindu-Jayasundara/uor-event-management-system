package com.uor.event_management_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "undergraduate_profile")

@Getter @Setter
public class UndergraduateProfileEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @ManyToOne
    @JoinColumn(name = "study_year_id")
    private StudyYearEntity studyYear;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
