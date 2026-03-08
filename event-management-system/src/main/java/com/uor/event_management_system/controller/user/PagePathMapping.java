package com.uor.event_management_system.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagePathMapping {

    @GetMapping("/user/dashboard")
    public String userDashboard(){
        return "user/dashboard";
    }
}
