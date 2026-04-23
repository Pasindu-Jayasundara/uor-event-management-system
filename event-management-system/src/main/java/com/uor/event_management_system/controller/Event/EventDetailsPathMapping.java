package com.uor.event_management_system.controller.Event;

import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.EventRegistrationRep;
import com.uor.event_management_system.repository.EventRepository;
import com.uor.event_management_system.repository.UserRepository;
import com.uor.event_management_system.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class EventDetailsPathMapping {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRegistrationService eventRegistration;

    @PostMapping("/event/{id}/cancel")
    public String cancelRegistration(@PathVariable int id, Principal principal) {

        UserEntity user = userRepository.findByEmail(principal.getName()).get();

        eventRegistration.cancelRegistration(user.getId(),id);

        return "redirect:/event/" + id;
    }





}
