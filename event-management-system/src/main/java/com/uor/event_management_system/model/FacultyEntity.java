package com.uor.event_management_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "faculty")

@Getter @Setter
public class FacultyEntity {

    @Id
    private int id;
    private String faculty;
    @OneToMany(mappedBy = "faculty")
    private List<DepartmentEntity> departments;
}
