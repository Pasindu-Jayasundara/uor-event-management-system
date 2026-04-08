package com.uor.event_management_system.controller.staff;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff")
public class StaffPathMapping {

    @GetMapping("/approval")
    public String approvals(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("page", "approval");
        model.addAttribute("user", userDetails);
        return "user/dashboard";
    }
}
