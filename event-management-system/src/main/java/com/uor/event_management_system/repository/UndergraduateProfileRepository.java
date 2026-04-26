package com.uor.event_management_system.repository;

import com.uor.event_management_system.model.UndergraduateProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UndergraduateProfileRepository extends JpaRepository<UndergraduateProfileEntity,Integer> {

    Optional<UndergraduateProfileEntity> findByUser_Id(int userId);





}
