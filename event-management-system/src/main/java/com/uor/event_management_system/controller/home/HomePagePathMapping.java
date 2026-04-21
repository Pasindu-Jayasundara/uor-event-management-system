package com.uor.event_management_system.controller.home;


import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.enums.EventStatus;
import com.uor.event_management_system.model.EventEntity;


import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.EventRegistrationRep;
import com.uor.event_management_system.repository.EventRepository;

import com.uor.event_management_system.repository.UserRepository;
import com.uor.event_management_system.service.EventRegistrationService;
import com.uor.event_management_system.service.EventService;
import com.uor.event_management_system.service.user.UserService;
import org.apache.catalina.User;
import org.apache.catalina.UserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.time.LocalDateTime;
import java.util.List;


@Controller
public class HomePagePathMapping {
    @Autowired
    private EventService service;


    @Autowired
    private EventRepository eventRepository;

    @Autowired
    UserService userService;

    @Autowired
    private EventRegistrationRep eventRegistrationRep;

    @Autowired
    private UserRepository userRepository;


    @ModelAttribute
    public void addAttributes(Model model, Principal principal){

        if(principal != null) {
            String email  = principal.getName();

            model.addAttribute("count", userService.CountRegisterdEvents(email));
        }
        model.addAttribute("events", service.getallEvents());
        model.addAttribute("eventCount",eventRepository.countByStatus(EventStatus.APPROVED));
        model.addAttribute("upcomingevents",service.UpcomingEvents());

    }












    @GetMapping("/")
    public String viewHome(Model model, Principal principal) {


        return "homepage";

    }


    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("events", service.searchEvents(keyword));
        return "homepage";
    }

    @GetMapping("/api/search")
    @ResponseBody
    public List<EventEntity> liveSearch(@RequestParam("keyword") String keyword) {
        return service.searchEvents(keyword);
    }


    @GetMapping("/category")
    public String filterByCategory(@RequestParam("type") int type, Model model) {
        model.addAttribute("events", service.getByCategory(type));
        return "homepage";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam("filter") String filter, Model model) {

        List<EventEntity> events;
        if(filter.equalsIgnoreCase("upcoming")) {

            events = service.getUpcomingEvents();

        }
        else if(filter.equalsIgnoreCase("past")) {
            events = service.getPastEvents();
        }

        else {
            events = service.getallEvents();
        }
        model.addAttribute("events", events);
        return "homepage";

    }


    @GetMapping("/event/{id}")
    public String viewEventDetails(@PathVariable int id, Model model, Principal principal) {
        boolean isRegistered = false;

        EventEntity event = eventRepository.findById(id).orElse(null);
        if(principal != null) {

            UserEntity user = userRepository.findByEmail(principal.getName()).get();

            isRegistered = eventRegistrationRep.existsByUser_IdAndEvent_IdAndStatus(user.getId(), event.getId(), EventRegistrationStatus.APPROVED);

        }
        model.addAttribute("isRegistered", isRegistered);

        model.addAttribute("event", event);

        return "event_details";
    }
















}
