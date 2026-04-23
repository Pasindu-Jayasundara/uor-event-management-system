package com.uor.event_management_system.repository;

import com.uor.event_management_system.model.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilesRepository extends JpaRepository<FilesEntity,Integer> {

    int countByEvent_Id(Integer eventId);
    List<FilesEntity> findByEvent_Id(Integer eventId);


}
