package com.uor.event_management_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Entity
@Table(name = "role")

@Getter @Setter
public class RoleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Length(max = 45) @NonNull
    private String role;
}
