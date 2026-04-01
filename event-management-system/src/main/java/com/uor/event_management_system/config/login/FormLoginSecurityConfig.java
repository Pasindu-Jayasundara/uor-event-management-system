package com.uor.event_management_system.config.login;

import com.uor.event_management_system.filter.LoginFilter;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.enums.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
public class FormLoginSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.addFilterBefore(new LoginFilter(),org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<org.springframework.security.config.annotation.web.builders.HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizationManagerRequestMatcherRegistry) {

                authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/admin/**", "/user/**").authenticated()
                        .requestMatchers("/css/**").permitAll()
                        .anyRequest().permitAll();
            }
        }).formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>>() {
            @Override
            public void customize(FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer) {

                httpSecurityFormLoginConfigurer
                        .loginPage("/login-page")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                                String role = authentication.getAuthorities().iterator().next().getAuthority();
                                UserEntity user = (UserEntity) authentication.getPrincipal();

                                if (role.equals(UserRole.ROLE_ADMIN.name())) {
                                    response.sendRedirect("/admin/dashboard");
                                } else if (role.equals(UserRole.ROLE_USER.name())) {
                                    response.sendRedirect("/user/dashboard");
                                } else if (role.equals(UserRole.ROLE_STAFF.name())) {

                                    if (user.isEnabled()) {
                                        response.sendRedirect("/user/dashboard");
                                    } else {
                                        response.sendRedirect("/login-page?error=true&msg=Account not verified");
                                    }

                                } else {
                                    response.sendRedirect("/");
                                }
                            }
                        })
                        .failureHandler(new AuthenticationFailureHandler() {
                            @Override
                            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                response.sendRedirect("/login-page?error=true&msg=" + exception.getMessage());
                            }
                        })
                        .permitAll();
            }
        }).logout(new Customizer<LogoutConfigurer<HttpSecurity>>() {
            @Override
            public void customize(LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer) {

                httpSecurityLogoutConfigurer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/");
            }
        });

        return http.build();
    }
}
