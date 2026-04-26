package com.uor.event_management_system.controller.Event;


import com.cloudinary.Cloudinary;
import com.uor.event_management_system.enums.EventStatus;
import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.model.OrganizeBy;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.EventCategoryRepository;
import com.uor.event_management_system.repository.EventRepository;
import com.uor.event_management_system.repository.OrganizeByRepository;
import com.uor.event_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Controller
public class EventAddPathMapping {

@Autowired
    EventCategoryRepository  eventCategoryRepository;

@Autowired
EventRepository eventRepository;

@Autowired
private UserRepository userRepository;

@Autowired

private OrganizeByRepository organizeByRepository;



    @GetMapping("/admin/add-event")
    public String showEventForm(Model model) {

        model.addAttribute("event", new EventEntity());

        model.addAttribute("categories", eventCategoryRepository.findAll());
        model.addAttribute("users", userRepository.findAll());

        return "admin/addEvent";
    }



    @PostMapping("/admin/add-event")
    public String saveEvent(@ModelAttribute EventEntity event ,  @RequestParam("organizerIds") List<Integer> userIds) {

        event.setStatus(EventStatus.PENDING);
        event.setRequestApproval(false);
        event.setHasLimit(false);

        EventEntity savedEvent = eventRepository.save(event);

        for (Integer userId : userIds) {

            UserEntity user = userRepository.findById(userId).orElse(null);

            OrganizeBy ob = new OrganizeBy();
            ob.setEvent(savedEvent);
            ob.setUser(user);

            organizeByRepository.save(ob);
        }

        return "redirect:/admin/dashboard";
    }



}
