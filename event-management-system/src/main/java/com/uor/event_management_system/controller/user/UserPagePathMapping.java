package com.uor.event_management_system.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPagePathMapping {

    @GetMapping("/user/dashboard")
    public String userDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model){
        model.addAttribute("user",userDetails);
        return "user/dashboard";
    }
}
