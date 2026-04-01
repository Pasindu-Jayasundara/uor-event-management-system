package com.uor.event_management_system.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.passay.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class LoginFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Check if user is ALREADY logged in
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);

        // 2. If they are logged in and trying to access the login page/path, redirect them away
        if (isAuthenticated && "/login-page".equals(request.getServletPath())) {
            response.sendRedirect("/");
            return;
        }

        if ("POST".equalsIgnoreCase(request.getMethod()) || "/login".equals(request.getServletPath())) {

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (email == null || email.isBlank() || password == null || password.isBlank()) {
                response.sendRedirect("/login-page?msg=Provide Authentication Details");
                return;
            }

            String ruhunaRegex = "^[a-zA-Z0-9._%+-]+@[a-z0-9]+\\.ruh\\.ac\\.lk$";

            if (!email.toLowerCase().matches(ruhunaRegex)) {
                response.sendRedirect("/login-page?msg=Must use a valid University of Ruhuna email");
                return;
            }

            PasswordValidator passwordValidator = new PasswordValidator(Arrays.asList(
                    new LengthRule(8, 30),
                    new CharacterRule(EnglishCharacterData.UpperCase, 1),
                    new CharacterRule(EnglishCharacterData.LowerCase, 1),
                    new CharacterRule(EnglishCharacterData.Digit, 1),
                    new CharacterRule(EnglishCharacterData.Special, 1),
                    new WhitespaceRule()
            ));

            RuleResult result = passwordValidator.validate(new PasswordData(password));
            if (!result.isValid()) {

                String failureMessage = String.join(", ", passwordValidator.getMessages(result));
                response.sendRedirect("/login-page?msg=" + java.net.URLEncoder.encode(failureMessage, "UTF-8"));
                return;
            }
        }

        filterChain.doFilter(request,response);

    }
}
