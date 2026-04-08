package com.uor.event_management_system.controller.home;

import com.uor.event_management_system.model.EventEntity;
import com.uor.event_management_system.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomePagePathMapping {
    @Autowired
    EventService service;

    @GetMapping("/homepage")
    public String viewHome(Model model) {
        model.addAttribute("events", service.getallEvents());
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
















}
