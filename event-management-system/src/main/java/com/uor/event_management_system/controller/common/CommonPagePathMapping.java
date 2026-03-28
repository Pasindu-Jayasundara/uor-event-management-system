package com.uor.event_management_system.controller.common;

import com.uor.event_management_system.dto.FacultyDepartmentDto;
import com.uor.event_management_system.dto.LoginDto;
import com.uor.event_management_system.dto.RegisterDto;
import com.uor.event_management_system.model.DepartmentEntity;
import com.uor.event_management_system.model.FacultyEntity;
import com.uor.event_management_system.repository.DepartmentRepository;
import com.uor.event_management_system.service.AccountTypeService;
import com.uor.event_management_system.service.DepartmentService;
import com.uor.event_management_system.service.FacultyService;
import com.uor.event_management_system.service.RegisterUserService;
import com.uor.event_management_system.util.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Controller
@SessionAttributes("registerDto")
public class CommonPagePathMapping {

    @Autowired
    private RegisterUserService registerUserService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AccountTypeService accountTypeService;




    @GetMapping("/login-page")
    public String loginPage(Model model) {

        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @GetMapping("/register-page")
    public String registerPage(Model model) {

        model.addAttribute("registerDto", new RegisterDto());
        model.addAttribute("accountTypeList", accountTypeService.getAllAccountTypes());
        model.addAttribute("step", 1);
        return "register";
    }

    @PostMapping("/register-page")
    public String registerPage(@ModelAttribute("registerDto") RegisterDto registerDto, Model model) {

        if(registerDto.getAccountType().equals("2")){// staff

            registerDto.setRole(UserRole.ROLE_STAFF.name());
            return "redirect:/register";

        }else{ // undergraduate

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
            model.addAttribute("step", 2);

            return "register";
        }

    }

    @PostMapping("/register")
    public String undergraduateRegister(@ModelAttribute("registerDto") RegisterDto registerDto, Model model, SessionStatus sessionStatus) {
        String status = registerUserService.registerUser(registerDto);

        if (status.equals("success")) {
            sessionStatus.setComplete();
            return "redirect:/login-page";
        } else {
            model.addAttribute("errorMsg", "Registration failed. Please try again.");
            return "redirect:/login-page";
        }
    }

    @GetMapping("/register")
    public String staffRegister(@ModelAttribute("registerDto") RegisterDto registerDto, Model model, SessionStatus sessionStatus) {
        String status = registerUserService.registerUser(registerDto);

        if (status.equals("success")) {
            sessionStatus.setComplete();
            return "redirect:/login-page";
        } else {
            model.addAttribute("errorMsg", "Registration failed. Please try again.");
            return "redirect:/login-page";
        }
    }
}
