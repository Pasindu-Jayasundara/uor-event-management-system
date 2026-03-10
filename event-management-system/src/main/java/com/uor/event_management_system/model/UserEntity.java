package com.uor.event_management_system.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="user")

@Getter @Setter
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Length(max = 45) @NonNull @Column(name = "first_name")
    private String firstName;

    @Length(max = 45) @NonNull @Column(name = "last_name")
    private String lastName;

    @Column(unique = true) @Length(max = 45) @NonNull
    private String email;

    @Column(nullable = false)
    private String password;

    @Length(max = 12) @NonNull
    private String nic;

    @ManyToOne
    @JoinColumn(name = "role_id",nullable = false)
    private RoleEntity role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRole()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
