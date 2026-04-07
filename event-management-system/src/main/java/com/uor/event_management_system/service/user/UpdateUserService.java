package com.uor.event_management_system.service.user;

import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserService {

    @Autowired
    private UserRepository userRepository;

    public void updateUser(UserEntity user) {
        userRepository.save(user);
    }
}
