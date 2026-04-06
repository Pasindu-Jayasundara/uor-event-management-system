package com.uor.event_management_system.controller.common;

import com.uor.event_management_system.annotation.ValidPasswordAnnotation;
import com.uor.event_management_system.dto.*;
import com.uor.event_management_system.enums.EmailTemplateType;
import com.uor.event_management_system.model.DepartmentEntity;
import com.uor.event_management_system.model.FacultyEntity;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.service.*;
import com.uor.event_management_system.enums.UserRole;
import com.uor.event_management_system.service.user.UpdateUserService;
import com.uor.event_management_system.util.SendEmail;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.*;
import java.util.function.Consumer;

@Controller
@SessionAttributes({"registerDto","registerUndergraduateDto","emailDto"})
public class CommonPagePathMapping {

    @Autowired
    private RegisterUserService registerUserService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AccountTypeService accountTypeService;

    @Autowired
    private SendEmail sendEmail;

    @Autowired
    private CheckDataAvailabilityService checkDataAvailability;

    @Autowired
    private UpdateUserService updateUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;



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
    public String forgotPasswordPage(Model model) {

        model.addAttribute("emailDto", new EmailDto());
        model.addAttribute("step", 1);
        return "forgot-password";
    }

    // STEP 1 - SEND OTP
    @PostMapping("/forgot-password")
    public String sendOtp(@Valid @ModelAttribute("emailDto") EmailDto emailDto,
                          BindingResult result,
                          Model model,
                          HttpServletRequest request) {

        if (result.hasErrors()) {
            model.addAttribute("step", 1);
            return "forgot-password";
        }

        UserEntity user = checkDataAvailability.findUserByEmail(emailDto.getTo());

        if (user == null) {
            model.addAttribute("msg", "User not found");
            model.addAttribute("step", 1);
            return "forgot-password";
        }

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        request.getSession().setAttribute("otp", otp);
        request.getSession().setAttribute("email", emailDto.getTo());

        try {

            Map<String, Object> map = new HashMap<>();
            map.put("firstName", user.getFirstName());
            map.put("otp", otp);
            map.put("requestTime", new Date());
            map.put("ipAddress", request.getRemoteAddr());

            emailDto.setVariables(map);
            emailDto.setEmailTemplateType(EmailTemplateType.FORGOT_PASSWORD_EMAIL_TEMPLATE);
            emailDto.setSubject("UniEvents - Forgot Password Reset Code");


            sendEmail.sendHtmlEmail(emailDto);

        } catch (Exception e) {
            model.addAttribute("msg", "Failed to send email");
            model.addAttribute("step", 1);
            return "forgot-password";
        }

        model.addAttribute("step", 2);
        return "forgot-password";
    }

    // STEP 2 - VERIFY OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp,
                            HttpServletRequest request,
                            Model model) {

        String sessionOtp = (String) request.getSession().getAttribute("otp");

        if (sessionOtp == null || !sessionOtp.equals(otp)) {
            model.addAttribute("msg", "Invalid OTP");
            model.addAttribute("step", 2);
            return "forgot-password";
        }

        model.addAttribute("resetPasswordDto",new ResetPasswordDto());
        model.addAttribute("step", 3);
        return "forgot-password";
    }

    // STEP 3 - RESET PASSWORD
    @PostMapping("/reset-password")
    public String resetPassword(@Valid @ModelAttribute("resetPasswordDto") ResetPasswordDto dto,
                                BindingResult result,
                                HttpServletRequest request,
                                Model model) {

        if (result.hasErrors()) {
            model.addAttribute("msg", result.getAllErrors().get(0).getDefaultMessage());
            model.addAttribute("step", 3);
            return "forgot-password";
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("msg", "Passwords do not match");
            model.addAttribute("step", 3);
            return "forgot-password";
        }

        String email = (String) request.getSession().getAttribute("email");
        UserEntity user = checkDataAvailability.findUserByEmail(email);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        updateUserService.updateUser(user);

        request.getSession().invalidate();

        model.addAttribute("step", 4);
        return "forgot-password";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordGet(Model model, HttpServletRequest request) {
        request.getSession().removeAttribute("otp");
        request.getSession().removeAttribute("email");
        model.addAttribute("emailDto", new EmailDto());
        model.addAttribute("step", 1);
        return "forgot-password";
    }
}
