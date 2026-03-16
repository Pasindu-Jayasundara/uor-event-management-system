package com.uor.event_management_system.controller.common;

import com.uor.event_management_system.dto.FacultyDepartmentDto;
import com.uor.event_management_system.dto.LoginDto;
import com.uor.event_management_system.dto.RegisterDto;
import com.uor.event_management_system.model.DepartmentEntity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Controller
public class CommonPagePathMapping {

    @Autowired
    private RegisterUserService registerUserService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/login-page")
    public String loginPage(Model model) {

        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @GetMapping("/register-page")
    public String registerPage(Model model) {

        model.addAttribute("registerDto", new RegisterDto());
        model.addAttribute("step", 1);
        return "register";
    }

    @PostMapping("/register-page")
    public String registerPage_2(@ModelAttribute("registerDto") RegisterDto registerDto, Model model) {

        model.addAttribute("registerDto", registerDto);
        model.addAttribute("step", 2);

        List<FacultyDepartmentDto> facultyDepartmentList = new ArrayList<>();

        facultyService.getAllFaculties().forEach(new Consumer<FacultyEntity>() {
            @Override
            public void accept(FacultyEntity facultyEntity) {

                List<DepartmentEntity> departments = departmentService.getDepartmentsByFacultyId(facultyEntity.getId());

                FacultyDepartmentDto facultyDepartmentDto = new FacultyDepartmentDto();
                facultyDepartmentDto.setFaculty(facultyEntity);
                facultyDepartmentDto.setDepartments(departments);

                facultyDepartmentList.add(facultyDepartmentDto);
            }
        });

        model.addAttribute("facultyDepartmentList", facultyDepartmentList);

        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerDto") RegisterDto registerDto, Model model) {
        String status = registerUserService.registerUser(registerDto);

        if (status.equals("success")) {
            return "redirect:/login-page";
        } else {
            model.addAttribute("errorMsg", "Registration failed. Please try again.");
            return "register";
        }
    }
}
