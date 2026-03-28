package com.uor.event_management_system.repository;

import com.uor.event_management_system.model.StaffProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffProfileRepository extends JpaRepository<StaffProfileEntity,Integer> {
}
