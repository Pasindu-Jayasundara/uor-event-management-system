package com.uor.event_management_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "department")

@Getter @Setter
public class DepartmentEntity implements Serializable {

    @Id
    private int id;
    private String department;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private FacultyEntity faculty;
}
