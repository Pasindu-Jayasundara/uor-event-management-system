package com.uor.event_management_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "faculty")

@Getter @Setter
public class FacultyEntity implements Serializable {

    @Id
    private int id;
    private String faculty;
}
