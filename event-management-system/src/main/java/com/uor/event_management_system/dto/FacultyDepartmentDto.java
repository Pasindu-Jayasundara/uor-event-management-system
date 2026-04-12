package com.uor.event_management_system.dto;

import com.uor.event_management_system.model.DepartmentEntity;
import com.uor.event_management_system.model.FacultyEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class FacultyDepartmentDto {

    private FacultyEntity faculty;
    private List<DepartmentEntity> departments;
}
