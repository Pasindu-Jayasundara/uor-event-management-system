package com.uor.event_management_system.controller.admin;

import com.uor.event_management_system.dto.PlatformUserDTO;
import com.uor.event_management_system.dto.UserSummaryDto;
import com.uor.event_management_system.enums.UserRole;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.service.admin.ManageUserService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RolesAllowed("ROLE_ADMIN")
@SessionAttributes({"user"})
public class AdminPagePathMapping {

    @Autowired
    private ManageUserService  manageUserService;




    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("page", "dashboard");
        model.addAttribute("user", userDetails);
        return "admin/dashboard";
    }

    @GetMapping("/approval")
    public String approvals(Model model) {
        model.addAttribute("page", "approval");
        return "admin/dashboard";
    }

    @GetMapping("/registration")
    public String registrations(Model model) {
        model.addAttribute("page", "registration");
        return "admin/dashboard";
    }

    @GetMapping("/manage-event")
    public String manageEvent(Model model) {
        model.addAttribute("page", "manage-event");
        return "admin/dashboard";
    }

    @GetMapping("/manage-user")
    public String manageUser(Model model) {
        model.addAttribute("page", "manage-user");


        UserSummaryDto platformUserSummary = manageUserService.getPlatformUserSummary();
        model.addAttribute("platformUserSummary", platformUserSummary);

        List<UserEntity> staffRegistrationRequestList = manageUserService.getStaffRegistrationRequestList();
        model.addAttribute("staffRegistrationRequestList", staffRegistrationRequestList);

        return "admin/dashboard";
    }

    @PostMapping("/approve-staff/{id}")
    public String approveStaff(@PathVariable int id) {

        manageUserService.approveStaffProfile(id);
        return "redirect:/admin/manage-user";
    }

    @PostMapping("/reject-staff/{id}")
    public String rejectStaff(@PathVariable int id) {

        manageUserService.rejectStaffProfile(id);
        return "redirect:/admin/manage-user";
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<PlatformUserDTO>> searchUsers(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean enabled) {

        List<PlatformUserDTO> results = manageUserService.searchUsers(q, role, enabled);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/enable-user/{id}")
    @ResponseBody
    public ResponseEntity<Void> enableUser(@PathVariable int id) {
        manageUserService.setUserEnabled(id, true);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/disable-user/{id}")
    @ResponseBody
    public ResponseEntity<Void> disableUser(@PathVariable int id) {
        manageUserService.setUserEnabled(id, false);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/promote")
    @ResponseBody
    public ResponseEntity<Void> promoteUser(@PathVariable int id) {
        manageUserService.changeUserRole(id, "ROLE_ADMIN");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/demote")
    @ResponseBody
    public ResponseEntity<Void> demoteUser(@PathVariable int id) {
        manageUserService.demoteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<PlatformUserDTO> getUser(@PathVariable int id) {
        PlatformUserDTO user = manageUserService.getUserById(id);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

}
