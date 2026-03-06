package com.uor.event_management_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Table(name="user")

@Getter @Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Length(max = 45) @NonNull
    private String first_name;

    @Length(max = 45) @NonNull
    private String last_name;

    @Column(unique = true) @Length(max = 45) @NonNull
    private String email;

    @Length(max = 45) @NonNull
    private String password;

    @Length(max = 12) @NonNull
    private String nic;

    @ManyToOne
    @JoinColumn(name = "role_id",nullable = false)
    private RoleEntity role_id;
}
