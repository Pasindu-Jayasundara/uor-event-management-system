package com.uor.event_management_system.repository;

import com.uor.event_management_system.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Integer> {

    RoleEntity findRoleByRole(String role);
    Optional<RoleEntity> findByRole(String role);
}
