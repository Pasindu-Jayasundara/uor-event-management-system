package com.uor.event_management_system.controller.common;

import com.uor.event_management_system.dto.LoginDto;
import com.uor.event_management_system.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PagePathMapping {

    @GetMapping("/login-page")
    public String login(Model model){

        model.addAttribute("loginDto",new LoginDto());
        return "login";
    }
}
