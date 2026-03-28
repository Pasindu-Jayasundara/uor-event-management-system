package com.uor.event_management_system.model;

import com.uor.event_management_system.util.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="user")

@Getter @Setter
public class UserEntity implements UserDetails, Serializable {

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

    @ManyToOne
    @JoinColumn(name = "role_id",nullable = false)
    private RoleEntity role;

    @ManyToOne
    @JoinColumn(name = "account_type_id",nullable = false)
    private AccountTypeEntity accountType;

//    @ManyToOne
//    @JoinColumn(name = "department_id")
//    private DepartmentEntity department;

//    @Column(name = "study_year", nullable = false)
//    private int studyYear;

    @OneToOne(mappedBy = "user")
    private StaffProfileEntity staffProfile;

    @OneToOne(mappedBy = "user")
    private UndergraduateProfileEntity undergraduateProfile;

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

        String type = accountType.getType();
        System.out.println(type);
        if (type.equals(AccountType.PROFILE_STAFF.name())) {
            return staffProfile != null && staffProfile.getVerified() == 1;
        }

        return true; // undergraduate allowed
    }
}
