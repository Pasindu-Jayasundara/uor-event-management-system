package com.uor.event_management_system.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPagePathMapping {

    @GetMapping("/admin/dashboard")
    public String adminDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model){
        model.addAttribute("user", userDetails);
        return "admin/dashboard";
    }
}
