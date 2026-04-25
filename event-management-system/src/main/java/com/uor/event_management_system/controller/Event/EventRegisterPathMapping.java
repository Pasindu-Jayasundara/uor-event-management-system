package com.uor.event_management_system.controller.Event;

import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.EventRepository;
import com.uor.event_management_system.repository.UserRepository;
import com.uor.event_management_system.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class EventRegisterPathMapping {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventRegistrationService  eventRegistrationService;


    @PostMapping("/event/{id}/register")
    public String registerEvent(@PathVariable int id, Principal principal) {

        if (principal == null) {
            return "redirect:/login"; // not logged in
        }

        UserEntity user = userRepository.findByEmail(principal.getName()).get();
        EventEntity event = eventRepository.findById(id).get();

        eventRegistrationService.register(user, event);

        return "redirect:/"; // reload homepage
    }


    @PostMapping("/event-details/{id}/register")
    public String registerEventDetails(@PathVariable int id, Principal principal) {

        if (principal == null) {
            return "redirect:/login"; // not logged in
        }

        UserEntity user = userRepository.findByEmail(principal.getName()).get();
        EventEntity event = eventRepository.findById(id).get();

        eventRegistrationService.register(user, event);

        return "redirect:/event/" + id;
    }

    @PostMapping("/event/{id}/userdashboadcancel")
    public String cancelRegistration(@PathVariable int id, Principal principal) {

        UserEntity user = userRepository.findByEmail(principal.getName()).get();

        eventRegistrationService.cancelRegistration(user.getId(),id);


        return "redirect:/user/dashboard"; // reload page
    }





}
