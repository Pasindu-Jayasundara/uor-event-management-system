package com.uor.event_management_system.service;

import com.uor.event_management_system.model.DepartmentEntity;
import com.uor.event_management_system.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<DepartmentEntity> getDepartmentsByFacultyId(Integer facultyId){
        return departmentRepository.getAllDepartmentsByFacultyId(facultyId);
    }
}
