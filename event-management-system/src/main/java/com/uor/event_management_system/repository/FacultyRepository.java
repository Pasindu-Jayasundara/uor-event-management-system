package com.uor.event_management_system.repository;

import com.uor.event_management_system.model.FacultyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<FacultyEntity,Integer> {
}
