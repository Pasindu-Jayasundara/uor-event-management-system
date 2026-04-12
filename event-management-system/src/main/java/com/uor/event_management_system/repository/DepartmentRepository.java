package com.uor.event_management_system.repository;

import com.uor.event_management_system.model.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity,Integer> {

    List<DepartmentEntity> getAllDepartmentsByFacultyId(Integer facultyId);
}
