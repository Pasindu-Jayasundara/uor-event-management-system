package com.uor.event_management_system.controller.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPagePathMapping {

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("page", "dashboard");
        model.addAttribute("user", userDetails);
        return "admin/dashboard";
    }

    @GetMapping("/approval")
    public String approvals(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("page", "approval");
        model.addAttribute("user", userDetails);
        return "admin/dashboard";
    }

    @GetMapping("/registration")
    public String registrations(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("page", "registration");
        model.addAttribute("user", userDetails);
        return "admin/dashboard";
    }

    @GetMapping("/manage-event")
    public String manageEvent(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("page", "manage-event");
        model.addAttribute("user", userDetails);
        return "admin/dashboard";
    }
}
