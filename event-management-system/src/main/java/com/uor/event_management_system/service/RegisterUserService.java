package com.uor.event_management_system.service;

import com.uor.event_management_system.dto.RegisterDto;
import com.uor.event_management_system.model.RoleEntity;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.RoleRepository;
import com.uor.event_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(RegisterDto registerDto){

        if(userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            return "Already Registered User";
        }

        RoleEntity role = roleRepository.findRoleByRole(registerDto.getRole());

        UserEntity newUser = new UserEntity();
        newUser.setFirst_name(registerDto.getFirst_name());
        newUser.setLast_name(registerDto.getLast_name());
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setNic(registerDto.getNic());
        newUser.setRole_id(role);

        try {
            userRepository.save(newUser);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }
}
