package com.uor.event_management_system.service;

import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckDataAvailabilityService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
