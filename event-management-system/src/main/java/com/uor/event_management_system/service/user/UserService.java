package com.uor.event_management_system.service.user;


import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.EventRegistrationRep;
import com.uor.event_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRegistrationRep eventRegistrationRep;

    public int CountRegisterdEvents(String email){
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            int count = eventRegistrationRep.countByUser_IdAndStatus(user.get().getId(), EventRegistrationStatus.APPROVED);
            return count;
        }
        return 0;

    }


    





}
