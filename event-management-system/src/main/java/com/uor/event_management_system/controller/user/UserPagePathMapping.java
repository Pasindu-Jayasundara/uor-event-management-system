package com.uor.event_management_system.controller.user;

import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.model.EventRegistration;
import com.uor.event_management_system.model.UndergraduateProfileEntity;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.EventRegistrationRep;
import com.uor.event_management_system.repository.EventRepository;
import com.uor.event_management_system.repository.UserRepository;
import com.uor.event_management_system.service.EventRegistrationService;
import com.uor.event_management_system.service.user.UserProfileService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
@RolesAllowed({"ROLE_USER","ROLE_STAFF"})
public class UserPagePathMapping {

    @Autowired
    private EventRegistrationService eventRegistrationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRegistrationRep registrationRepository;

    @Autowired
    private UserProfileService userProfileService;



    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).get();
        int Registerdcount = registrationRepository.countByUser_IdAndStatus(user.getId(), EventRegistrationStatus.APPROVED);
        List<EventRegistration> registeredEvents= eventRegistrationService.getUserRegistrations(user);
        UndergraduateProfileEntity profile = userProfileService.getProfile(user);

        model.addAttribute("profile", profile);
        model.addAttribute("registeredEvents", registeredEvents);
        model.addAttribute("page", "dashboard");

        model.addAttribute("user", user);

        model.addAttribute("registerdcount", Registerdcount);
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

    @PostMapping("/update-profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam String firstName,
                                @RequestParam String lastName) {

        userProfileService.updateProfile(userDetails.getUsername(), firstName, lastName);

        return "redirect:/user/dashboard#profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 RedirectAttributes redirectAttributes) {

        try {
            boolean success = userProfileService.changePassword(
                    userDetails.getUsername(), oldPassword, newPassword
            );

            if (!success) {
                redirectAttributes.addFlashAttribute("error", "❌ Old password is incorrect");
            } else {
                redirectAttributes.addFlashAttribute("success", "✅ Password updated successfully");
            }

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "❌ " + e.getMessage());
        }

        return "redirect:/user/dashboard#profile";
    }






}
