package com.uor.event_management_system.controller.home;


import com.uor.event_management_system.enums.EventRegistrationStatus;
import com.uor.event_management_system.enums.EventStatus;
import com.uor.event_management_system.model.EventEntity;


import com.uor.event_management_system.model.EventRegistration;
import com.uor.event_management_system.model.FilesEntity;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.EventRegistrationRep;
import com.uor.event_management_system.repository.EventRepository;

import com.uor.event_management_system.repository.UserRepository;
import com.uor.event_management_system.service.EventRegistrationService;
import com.uor.event_management_system.service.user.UserEventService;
import com.uor.event_management_system.service.FileService;
import com.uor.event_management_system.service.OrganizeByService;
import com.uor.event_management_system.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
public class HomePagePathMapping {
    @Autowired
    private UserEventService service;


    @Autowired
    private EventRepository eventRepository;

    @Autowired
    UserService userService;

    @Autowired
    private EventRegistrationRep eventRegistrationRep;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private OrganizeByService organizeByService;

    @Autowired
    private EventRegistrationService eventRegistrationService;


    @ModelAttribute
    public void addAttributes(Model model, Principal principal){

        Map<Integer, String> registrationStatus = new HashMap<>();
        if(principal != null) {
            String email  = principal.getName();
            UserEntity user = userRepository.findByEmail(email).get();
            registrationStatus = eventRegistrationService.getUserRegistrationStatus(user);
            model.addAttribute("count", userService.CountRegisterdEvents(email));
        }
        model.addAttribute("registrationStatus", registrationStatus);
        model.addAttribute("events", service.getallEvents());
        model.addAttribute("eventCount",eventRepository.countByStatus(EventStatus.APPROVED));
        model.addAttribute("upcomingevents",service.UpcomingEvents());

    }



    @GetMapping("/")
    public String viewHome(Model model, Principal principal) {


        return "homepage";

    }


    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("events", service.searchEvents(keyword));
        return "homepage";
    }

    @GetMapping("/api/search")
    @ResponseBody
    public List<EventEntity> liveSearch(@RequestParam("keyword") String keyword) {
        return service.searchEvents(keyword);
    }


    @GetMapping("/category")
    public String filterByCategory(@RequestParam("type") int type, Model model) {
        model.addAttribute("events", service.getByCategory(type));
        return "homepage";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam("filter") String filter, Model model) {

        List<EventEntity> events;
        if(filter.equalsIgnoreCase("upcoming")) {

            events = service.getUpcomingEvents();

        }
        else if(filter.equalsIgnoreCase("past")) {
            events = service.getPastEvents();
        }

        else {
            events = service.getallEvents();
        }
        model.addAttribute("events", events);
        return "homepage";

    }


    @GetMapping("/event/{id}")
    public String viewEventDetails(@PathVariable int id, Model model, Principal principal) {

        EventRegistrationStatus status = null;

        EventEntity event = eventRepository.findById(id).orElse(null);
        if (principal != null) {

            UserEntity user = userRepository.findByEmail(principal.getName()).get();

            Optional<EventRegistration> reg =
                    eventRegistrationRep.findByUser_IdAndEvent_Id(user.getId(), id);


            if (reg.isPresent()) {
                status = reg.get().getStatus();
            }


            // isRegistered = eventRegistrationRep.existsByUser_IdAndEvent_IdAndStatus(user.getId(), event.getId(), EventRegistrationStatus.APPROVED);

        }

        List<UserEntity> organizers = organizeByService.getOrganizers(id);


        List<FilesEntity> evfiles = fileService.getFiles(id);

        model.addAttribute("organizers", organizers);

        model.addAttribute("files", evfiles);

        model.addAttribute("status", status);

        model.addAttribute("event", event);

        return "event_details";

    }

    // ===== 2. REGISTER FOR EVENT =====
    /**
     * POST /api/register
     * Params: userId, eventId
     * Returns: status (REGISTERED, PENDING, WAITLIST, etc.)
     */
    @PostMapping("/api/register")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam int userId,
            @RequestParam int eventId) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Call service to register
            String result = eventRegistrationService.register(userId, eventId);

            // Check if registration was successful
            if (result.equalsIgnoreCase("REGISTERED") ||
                    result.equalsIgnoreCase("PENDING") ||
                    result.equalsIgnoreCase("WAITLIST")) {

                response.put("success", true);
                response.put("status", result);
                response.put("message", getStatusMessage(result));

                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(response);
            } else {
                // Error case (already registered, etc.)
                response.put("success", false);
                response.put("status", "ERROR");
                response.put("message", result);

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("status", "ERROR");
            response.put("message", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    // ===== 3. APPROVE REGISTRATION (ADMIN) =====
    /**
     * PUT /api/approve/{registrationId}
     * Returns: new status (REGISTERED, WAITLIST, etc.)
     */
    @PutMapping("/api/approve/{registrationId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> approveRegistration(
            @PathVariable int registrationId) {

        Map<String, Object> response = new HashMap<>();

        try {
            String result = eventRegistrationService.approve(registrationId);

            // Check if it's an error message
            if (result.contains("already")) {
                response.put("success", false);
                response.put("message", result);
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

            response.put("success", true);
            response.put("status", result);
            response.put("message", "Registration approved!");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    // ===== 4. REJECT REGISTRATION (ADMIN) =====
    /**
     * PUT /api/reject/{registrationId}
     * Returns: REJECTED status
     */
    @PutMapping("/api/reject/{registrationId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rejectRegistration(
            @PathVariable int registrationId,
            @RequestParam(required = false) String remarks) {

        Map<String, Object> response = new HashMap<>();

        try {
            String result = eventRegistrationService.reject(registrationId);

            // Check if it's an error message
            if (result.contains("already")) {
                response.put("success", false);
                response.put("message", result);
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

            response.put("success", true);
            response.put("status", result);
            response.put("message", "Registration rejected!");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    // ===== 5. CANCEL REGISTRATION (USER) =====
    /**
     * DELETE /api/cancel
     * Params: userId, eventId
     * Returns: CANCELLED status
     */
    @DeleteMapping("/api/cancel")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cancelRegistration(
            @RequestParam int userId,
            @RequestParam int eventId) {

        Map<String, Object> response = new HashMap<>();

        try {
            String result = eventRegistrationService.cancel(userId, eventId);

            response.put("success", true);
            response.put("status", result);
            response.put("message", "Registration cancelled successfully!");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    // ===== 6. GET USER REGISTRATIONS =====
    /**
     * GET /api/user-registrations/{userId}
     * Returns: Map of eventId -> status
     */
    @GetMapping("/api/user-registrations/{userId}")
    @ResponseBody
    public ResponseEntity<Map<Integer, String>> getUserRegistrations(
            @PathVariable int userId) {

        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Map<Integer, String> registrations =
                    eventRegistrationService.getUserRegistrationStatus(user);

            return ResponseEntity.ok(registrations);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new HashMap<>());
        }
    }

    // ===== 7. GET EVENT REGISTRATIONS =====
    /**
     * GET /api/event-registrations/{eventId}
     * Returns: List of all registrations for event
     */
    @GetMapping("/api/event-registrations/{eventId}")
    @ResponseBody
    public ResponseEntity<List<EventRegistration>> getEventRegistrations(
            @PathVariable int eventId) {

        try {
            List<EventRegistration> registrations =
                    eventRegistrationService.getEventRegistrations(eventId);

            return ResponseEntity.ok(registrations);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new java.util.ArrayList<>());
        }
    }

    // ===== 8. GET PENDING REGISTRATIONS (ADMIN) =====
    /**
     * GET /api/event-registrations/{eventId}/pending
     * Returns: List of pending registrations for event
     */
    @GetMapping("/api/event-registrations/{eventId}/pending")
    @ResponseBody
    public ResponseEntity<List<EventRegistration>> getPendingRegistrations(
            @PathVariable int eventId) {

        try {
            List<EventRegistration> registrations =
                    eventRegistrationService.getByEvent_IdAndStatus(
                            eventId, EventRegistrationStatus.PENDING);

            return ResponseEntity.ok(registrations);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new java.util.ArrayList<>());
        }
    }

    // ===== 9. GET REGISTERED USERS =====
    /**
     * GET /api/event-registrations/{eventId}/registered
     * Returns: List of registered users for event
     */
    @GetMapping("/api/event-registrations/{eventId}/registered")
    @ResponseBody
    public ResponseEntity<List<EventRegistration>> getRegisteredUsers(
            @PathVariable int eventId) {

        try {
            List<EventRegistration> registrations =
                    eventRegistrationService.getByEvent_IdAndStatus(
                            eventId, EventRegistrationStatus.REGISTERED);

            return ResponseEntity.ok(registrations);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new java.util.ArrayList<>());
        }
    }

    // ===== 10. GET WAITLIST =====
    /**
     * GET /api/event-registrations/{eventId}/waitlist
     * Returns: List of waitlisted users for event
     */
    @GetMapping("/api/event-registrations/{eventId}/waitlist")
    @ResponseBody
    public ResponseEntity<List<EventRegistration>> getWaitlist(
            @PathVariable int eventId) {

        try {
            List<EventRegistration> registrations =
                    eventRegistrationService.getByEvent_IdAndStatus(
                            eventId, EventRegistrationStatus.WAITLIST);

            return ResponseEntity.ok(registrations);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new java.util.ArrayList<>());
        }
    }

    // ===== 11. GET REGISTRATION COUNT BY STATUS =====
    /**
     * GET /api/registration-count/{eventId}
     * Params: status (REGISTERED, PENDING, WAITLIST, etc.)
     */
    @GetMapping("/api/registration-count/{eventId}")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> getRegistrationCount(
            @PathVariable int eventId,
            @RequestParam(required = false) String status) {

        Map<String, Integer> response = new HashMap<>();

        try {
            if (status != null && !status.isEmpty()) {
                // Count specific status
                int count = eventRegistrationService.getRegistrationCount(
                        eventId,
                        EventRegistrationStatus.valueOf(status));
                response.put("count", count);
            } else {
                // Count all statuses
                int registered = eventRegistrationService.getRegistrationCount(
                        eventId, EventRegistrationStatus.REGISTERED);
                int pending = eventRegistrationService.getRegistrationCount(
                        eventId, EventRegistrationStatus.PENDING);
                int waitlist = eventRegistrationService.getRegistrationCount(
                        eventId, EventRegistrationStatus.WAITLIST);

                response.put("registered", registered);
                response.put("pending", pending);
                response.put("waitlist", waitlist);
                response.put("total", registered + pending + waitlist);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", 0);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    // ===== 12. CHECK REGISTRATION STATUS =====
    /**
     * GET /api/registration-status
     * Params: userId, eventId
     * Returns: current status
     */
    @GetMapping("/api/registration-status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkRegistrationStatus(
            @RequestParam int userId,
            @RequestParam int eventId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Optional<EventRegistration> registration =
                    eventRegistrationRep.findByUser_IdAndEvent_Id(userId, eventId);

            if (registration.isPresent()) {
                EventRegistration reg = registration.get();
                response.put("registered", true);
                response.put("status", reg.getStatus().name());
                response.put("registrationId", reg.getId());
               // response.put("registeredAt", reg.getRegisteredAt());
                return ResponseEntity.ok(response);
            } else {
                response.put("registered", false);
                response.put("status", "NOT_REGISTERED");
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            response.put("registered", false);
            response.put("error", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    // =====================================================
    //  HELPER METHODS
    // =====================================================

    /**
     * Convert status to user-friendly message
     */
    private String getStatusMessage(String status) {
        return switch (status) {
            case "REGISTERED" -> "✅ Successfully registered! You can now attend this event.";
            case "PENDING" -> "⏳ Your registration is pending admin approval. You will be notified once approved.";
            case "WAITLIST" -> "📋 Event is full! You have been added to the waitlist. We will notify you if a spot becomes available.";
            case "REJECTED" -> "❌ Your registration was rejected.";
            case "CANCELLED" -> "🔄 Your registration has been cancelled.";
            default -> status;
        };
    }
















}
