package com.uor.event_management_system.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LoginFilter extends OncePerRequestFilter {

    // shouldNotFilter restricts this filter to POST /login requests only,
    // so other POST endpoints (e.g. /register) are not affected.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !("POST".equalsIgnoreCase(request.getMethod())
                && "/login".equals(request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Check if user is ALREADY logged in
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);

        // 2. If they are logged in and trying to access the login page/path, redirect them away
        if (isAuthenticated) {
            response.sendRedirect("/");
            return;
        }

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            response.sendRedirect("/login-page?msg=" + URLEncoder.encode("Provide Authentication Details", StandardCharsets.UTF_8));
            return;
        }

        String ruhunaRegex = "^[a-zA-Z0-9._%+-]+@[a-z0-9]+\\.ruh\\.ac\\.lk$";

        if (!email.toLowerCase().matches(ruhunaRegex)) {
            response.sendRedirect("/login-page?msg=" + URLEncoder.encode("Must use a valid University of Ruhuna email", StandardCharsets.UTF_8));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
