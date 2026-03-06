package com.uor.event_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagePathMapping {

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(){
        return "admin/dashboard";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(){
        return "user/dashboard";
    }

}
