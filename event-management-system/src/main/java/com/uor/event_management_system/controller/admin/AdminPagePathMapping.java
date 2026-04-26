package com.uor.event_management_system.controller.admin;

import com.uor.event_management_system.dto.*;
import com.uor.event_management_system.model.EventCategory;
import com.uor.event_management_system.enums.EventStatus;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.service.EventCategoryService;
import com.uor.event_management_system.service.admin.AdminEventService;
import com.uor.event_management_system.service.admin.DashboardService;
import com.uor.event_management_system.service.admin.ManageUserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RolesAllowed("ROLE_ADMIN")
@SessionAttributes({"user"})
public class AdminPagePathMapping {

    @Autowired
    private ManageUserService  manageUserService;

    @Autowired
    private AdminEventService adminEventService;

    @Autowired
    private EventCategoryService eventCategoryService;

    @Autowired
    private DashboardService dashboardService;



    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("page", "dashboard");
        model.addAttribute("user", userDetails);

        DashboardDto dashboard = dashboardService.buildDashboard();
        model.addAttribute("dashboard", dashboard);

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

//    @GetMapping("/manage-event")
//    public String manageEvent(Model model) {
//        model.addAttribute("page", "manage-event");
//        return "admin/dashboard";
//    }

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



    //EVENT MANAGEMENT
    @GetMapping("/manage-event")
    public String manageEvent(
            @RequestParam (required = false) String search,
            @RequestParam (required = false) EventCategory category,
            @RequestParam (required = false, defaultValue ="table") String view,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        List<EventResponseDto> events;
        if (search != null && !search.isBlank()) {
            events = adminEventService.searchEvent(search);
        } else if (category != null) {
            events = adminEventService.filterByCategory(category);
        }else {
            events = adminEventService.getAllEvents();
        }

        model.addAttribute("page", "manage-event");
        model.addAttribute("user", userDetails);
        model.addAttribute("events", events);
        model.addAttribute("totalEvents", adminEventService.getTotalCount());
        model.addAttribute("search",search);
        model.addAttribute("category",category);
        model.addAttribute("statuses", EventStatus.values());
        model.addAttribute("currentView", view);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("newEvent", new EventRequestDto());
        return "admin/dashboard";


    }

    @PostMapping("/manage-event/create")
    public String createEvent(
            @Valid @ModelAttribute("newEvent") EventRequestDto dto,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectionAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("page", "manage-event");
            model.addAttribute("user", userDetails);
            model.addAttribute("events", adminEventService.getAllEvents());
            model.addAttribute("totalEvents", adminEventService.getTotalCount());
            model.addAttribute("categories", eventCategoryService.getAllCategories());
            model.addAttribute("statuses", EventStatus.values());
            model.addAttribute("currentView", "Table");
            model.addAttribute("newEvent", dto);
            model.addAttribute("showCreateModal", true);
            return "admin/dashboard";
        }
        try{
            adminEventService.createEvent(dto);
            redirectionAttributes.addFlashAttribute("successMessage","Event \"" + dto.getTitle() + "\"  created successfully");
        } catch (Exception e) {
            redirectionAttributes.addFlashAttribute("errorMessage", "Failed to create event : " + e.getMessage());
        }
        return "redirect:/admin/manage-event";
    }

    @PutMapping("/manage-event/edit/{id}")
    public String updateEvent(
        @PathVariable int id,
        @Valid @ModelAttribute EventRequestDto dto,
        BindingResult result,
        @AuthenticationPrincipal UserDetails userDetails,
        RedirectAttributes redirectAttributes,
        Model model){

        if(result.hasErrors()) {
            model.addAttribute("page", "manage-event");
            model.addAttribute("user", userDetails);
            model.addAttribute("events", adminEventService.getAllEvents());
            model.addAttribute("totalEvents", adminEventService.getTotalCount());
            //model.addAttribute("categories", EventCategory.values());

            model.addAttribute("statuses", EventStatus.values());
            model.addAttribute("currentView", "Table");
            model.addAttribute("newEvent", dto);
            model.addAttribute("showCreateModal", true);
            model.addAttribute("editEventId", id);
            return "admin/dashboard";
        }
        try{
            adminEventService.updateEvent(id, dto);
            redirectAttributes.addFlashAttribute("successMessage","Event \"" + dto.getTitle() + "\"  updated successfully");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update event : " + e.getMessage());

        }
        return "redirect:/admin/manage-event";

    }

    @DeleteMapping("/manage-event/delete/{id}")
    public String deleteEvent(@RequestParam int id, RedirectAttributes redirectAttributes) {
        try {
            adminEventService.deleteEvent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Event deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete event : " + e.getMessage());
        }
        return "redirect:/admin/manage-event";
    }

    @GetMapping("/manage-event/view/{id}")
    public String viewEvent(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails , Model model) {
        model.addAttribute("page", "manage-event-details");
        model.addAttribute("user", userDetails);

        try{
            model.addAttribute("page", adminEventService.getEventById(id));
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to view event : " + e.getMessage());
        }
        return "admin/dashboard";
    }

    @GetMapping("/manage-event/api/{id}")
    @ResponseBody
    public ResponseEntity<?> getEventApi(@PathVariable int id){
        try{
            return ResponseEntity.ok(adminEventService.getEventForEdit(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
