package com.uor.event_management_system.repository;

import com.uor.event_management_system.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    Optional<UserEntity> findByEmail(String email);

    int countBy(); // total users
    int countByEnabled(int enabled); // enabled / disabled users

    @Query("SELECT u FROM UserEntity u JOIN u.staffProfile s WHERE s.verified = 0 AND u.enabled = 1")
    List<UserEntity> getNotVerifiedStaffList();
}
