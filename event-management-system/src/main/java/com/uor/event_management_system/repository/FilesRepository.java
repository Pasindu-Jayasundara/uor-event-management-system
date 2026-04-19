package com.uor.event_management_system.repository;

import com.uor.event_management_system.model.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesRepository extends JpaRepository<FilesEntity,Integer> {

    int countByEvent_Id(Integer eventId);


}
