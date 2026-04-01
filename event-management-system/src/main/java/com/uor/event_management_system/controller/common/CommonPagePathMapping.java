package com.uor.event_management_system.controller.common;

import com.uor.event_management_system.dto.FacultyDepartmentDto;
import com.uor.event_management_system.dto.LoginDto;
import com.uor.event_management_system.dto.RegisterDto;
import com.uor.event_management_system.dto.RegisterUndergraduateDto;
import com.uor.event_management_system.model.DepartmentEntity;
import com.uor.event_management_system.model.FacultyEntity;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.service.AccountTypeService;
import com.uor.event_management_system.service.DepartmentService;
import com.uor.event_management_system.service.FacultyService;
import com.uor.event_management_system.service.RegisterUserService;
import com.uor.event_management_system.enums.UserRole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Controller
@SessionAttributes({"registerDto","registerUndergraduateDto"})
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
    public String registerPage(@Valid @ModelAttribute("registerDto") RegisterDto registerDto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("accountTypeList", accountTypeService.getAllAccountTypes());
            model.addAttribute("step", 1);

            return "register";
        }

        if("2".equals(String.valueOf(registerDto.getAccountType()))){// staff

            registerDto.setRole(UserRole.ROLE_STAFF.name());
            return "redirect:/register";

        }else{ // undergraduate

            List<FacultyDepartmentDto> facultyDepartmentList = getFacultyDepartmentList();

            model.addAttribute("facultyDepartmentList", facultyDepartmentList);
            model.addAttribute("registerUndergraduateDto", new RegisterUndergraduateDto());
            model.addAttribute("step", 2);

            return "register";
        }

    }

    @PostMapping("/register")
    public String undergraduateRegister(@ModelAttribute("registerDto") RegisterDto registerDto,
                                        @Valid @ModelAttribute("registerUndergraduateDto") RegisterUndergraduateDto registerUndergraduateDto, BindingResult result, Model model, SessionStatus sessionStatus) {

        if (result.hasErrors()) {

            List<FacultyDepartmentDto> facultyDepartmentList = getFacultyDepartmentList();
            model.addAttribute("facultyDepartmentList",facultyDepartmentList);
            model.addAttribute("step", 2);

            return "register";
        }

        HashMap<String, Optional<UserEntity>> statusMap = registerUserService.registerUser(registerDto);

        boolean userSaved = false;
        if(statusMap.containsKey("User saved")){
            userSaved = registerUserService.registerUndergraduateUser(statusMap.get("User saved").get(),registerUndergraduateDto);
        }

        if (userSaved) {
            sessionStatus.setComplete();
            return "redirect:/login-page";
        } else {
            model.addAttribute("errorMsg", "Registration failed. Please try again.");
            return "redirect:/login-page";
        }
    }

    @GetMapping("/register")
    public String staffRegister(@Valid @ModelAttribute("registerDto") RegisterDto registerDto, BindingResult result, Model model, SessionStatus sessionStatus) {

        if (result.hasErrors()) {

            model.addAttribute("accountTypeList",accountTypeService.getAllAccountTypes());
            model.addAttribute("step", 1);

            return "register";
        }

        HashMap<String, Optional<UserEntity>> statusMap = registerUserService.registerUser(registerDto);
        boolean userSaved = false;
        if(statusMap.containsKey("User saved")){
            userSaved = registerUserService.registerStaffUser(statusMap.get("User saved").get());
        }

        if (userSaved) {
            sessionStatus.setComplete();
            return "redirect:/login-page";
        } else {
            model.addAttribute("errorMsg", "Registration failed. Please try again.");
            return "redirect:/login-page";
        }
    }

    private List<FacultyDepartmentDto> getFacultyDepartmentList(){

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
        return facultyDepartmentList;
    }

    @GetMapping("/forgot-password-page")
    public String forgotPasswordPage(){
        return "forgot-password";
    }

    @GetMapping("/et")
    public String et(){
        return "email/forgot-password-email-template";
    }
}
