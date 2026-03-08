package com.uor.event_management_system.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPagePathMapping {

    @GetMapping("/admin/dashboard")
    public String adminDashboard(){
        return "admin/dashboard";
    }
}
