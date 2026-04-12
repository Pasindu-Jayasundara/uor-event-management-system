package com.uor.event_management_system.repository;

import com.uor.event_management_system.model.StudyYearEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyYearRepository extends JpaRepository<StudyYearEntity,Integer> {
}
