package com.uor.event_management_system.service;

import com.uor.event_management_system.model.FacultyEntity;
import com.uor.event_management_system.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private DepartmentService departmentService;

    public List<FacultyEntity> getAllFacultiesWithDepartments(){
        return facultyRepository.findAll();
    }
}
