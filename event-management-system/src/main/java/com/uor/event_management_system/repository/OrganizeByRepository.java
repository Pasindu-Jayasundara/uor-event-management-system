package com.uor.event_management_system.repository;

import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.model.OrganizeBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizeByRepository extends JpaRepository<OrganizeBy, Integer> {

    List<OrganizeBy> findByEvent_Id(int eventId);



}
