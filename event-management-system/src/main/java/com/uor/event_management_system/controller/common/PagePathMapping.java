package com.uor.event_management_system.controller.common;

import com.uor.event_management_system.dto.LoginDto;
import com.uor.event_management_system.dto.RegisterDto;
import com.uor.event_management_system.service.LoginUserService;
import com.uor.event_management_system.service.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PagePathMapping {

    @Autowired
    private RegisterUserService registerUserService;

    @GetMapping("/login-page")
    public String loginPage(Model model){

        model.addAttribute("loginDto",new LoginDto());
        return "login";
    }

    @GetMapping("/register-page")
    public String registerPage(Model model){

        model.addAttribute("registerDto",new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerDto") RegisterDto registerDto, Model model){
        String status = registerUserService.registerUser(registerDto);

        if(status.equals("success")) {
            return "redirect:/login-page";
        } else {
            model.addAttribute("errorMsg", "Registration failed. Please try again.");
            return "register";
        }
    }
}
