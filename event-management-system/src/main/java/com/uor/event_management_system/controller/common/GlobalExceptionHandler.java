package com.uor.event_management_system.controller.common;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleAll(Exception ex, Model model) {
//            model.addAttribute("exception", ex.getClass().getName() + ": " + ex.getMessage());
        model.addAttribute("errorId", "ERR-" + System.currentTimeMillis());
        return "error"; // resolves to templates/error.html
    }
}