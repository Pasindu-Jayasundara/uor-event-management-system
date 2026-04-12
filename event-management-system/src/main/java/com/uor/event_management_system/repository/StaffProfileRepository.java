package com.uor.event_management_system.repository;

import com.uor.event_management_system.model.StaffProfileEntity;
import com.uor.event_management_system.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffProfileRepository extends JpaRepository<StaffProfileEntity,Integer> {

    StaffProfileEntity findByUser(UserEntity user);
}
