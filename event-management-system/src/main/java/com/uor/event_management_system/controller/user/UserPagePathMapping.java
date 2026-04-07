package com.uor.event_management_system.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserPagePathMapping {

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("page", "dashboard");
        model.addAttribute("user", userDetails);
        return "user/dashboard";
    }

    @GetMapping("/event")
    public String approvals(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("page", "event");
        model.addAttribute("user", userDetails);
        return "user/dashboard";
    }

    @GetMapping("/my-registration")
    public String registrations(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("page", "my-registration");
        model.addAttribute("user", userDetails);
        return "user/dashboard";
    }
}
