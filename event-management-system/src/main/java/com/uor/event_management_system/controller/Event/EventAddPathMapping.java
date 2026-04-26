package com.uor.event_management_system.controller.Event;


import com.uor.event_management_system.model.EventEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EventAddPathMapping {



    @PostMapping ("/admin/add-event")
    public String showEventForm(Model model) {

        model.addAttribute("event", new EventEntity());



        return "admin/event-form";
    }





}
