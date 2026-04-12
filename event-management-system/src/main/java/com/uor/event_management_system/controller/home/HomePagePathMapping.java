package com.uor.event_management_system.controller.home;


import com.uor.event_management_system.enums.EventStatus;
import com.uor.event_management_system.model.EventEntity;



import com.uor.event_management_system.repository.EventRepository;

import com.uor.event_management_system.service.EventService;
import com.uor.event_management_system.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String filterByCategory(@RequestParam("type") String type, Model model) {
        model.addAttribute("events", service.getByCategory(type));
        return "homepage";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam("filter") String filter, Model model) {

        List<EventEntity> events;
        if(filter.equalsIgnoreCase("upcoming")) {

            events = service.findByEventDateTimeAfter(LocalDateTime.now());

        }
        else if(filter.equalsIgnoreCase("past")) {
            events = service.findByEventDateTimeBefore(LocalDateTime.now());
        }

        else {
            events = service.getallEvents();
        }
        model.addAttribute("events", events);
        return "homepage";

    }
















}
