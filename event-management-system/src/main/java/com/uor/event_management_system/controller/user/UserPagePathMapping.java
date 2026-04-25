package com.uor.event_management_system.controller.user;

import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.model.EventRegistration;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.EventRegistrationRep;
import com.uor.event_management_system.repository.EventRepository;
import com.uor.event_management_system.repository.UserRepository;
import com.uor.event_management_system.service.EventRegistrationService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).get();
        int Registerdcount = registrationRepository.countByUser_IdAndStatus(user.getId(), EventRegistrationStatus.APPROVED);
        List<EventRegistration> registeredEvents= eventRegistrationService.getUserRegistrations(user);
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
}
