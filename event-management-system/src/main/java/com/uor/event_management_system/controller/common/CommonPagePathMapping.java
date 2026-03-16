package com.uor.event_management_system.controller.common;

import com.uor.event_management_system.dto.FacultyDepartmentDto;
import com.uor.event_management_system.dto.LoginDto;
import com.uor.event_management_system.dto.RegisterDto;
import com.uor.event_management_system.model.FacultyEntity;
import com.uor.event_management_system.repository.DepartmentRepository;
import com.uor.event_management_system.service.DepartmentService;
import com.uor.event_management_system.service.FacultyService;
import com.uor.event_management_system.service.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CommonPagePathMapping {

    @Autowired
    private RegisterUserService registerUserService;

    @Autowired
    private FacultyService facultyService;

    @GetMapping("/login-page")
    public String loginPage(Model model){

        model.addAttribute("loginDto",new LoginDto());
        return "login";
    }

    @GetMapping("/register-page")
    public String registerPage(Model model){

//        if(!model.containsAttribute("registerDto")){
            model.addAttribute("registerDto", new RegisterDto());
//        }

        return "register";
    }

    @PostMapping("/register-page")
    public String registerPage_2(@RequestParam(value = "step") Integer step, Model model){

        if(!model.containsAttribute("registerDto")){
            model.addAttribute("registerDto", new RegisterDto());
        }else{
            model.addAttribute("registerDto", model.getAttribute("registerDto"));
        }
//
//        if(step != null && step == 2){
            List<FacultyEntity> facultyDepartmentList = facultyService.getAllFacultiesWithDepartments();
            model.addAttribute("facultyDepartmentList", facultyDepartmentList);
//        }

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
