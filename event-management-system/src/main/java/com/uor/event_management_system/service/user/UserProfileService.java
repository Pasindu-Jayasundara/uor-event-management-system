package com.uor.event_management_system.service.user;

import com.uor.event_management_system.model.UndergraduateProfileEntity;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.UndergraduateProfileRepository;
import com.uor.event_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private UndergraduateProfileRepository profileRepo;

    @Autowired
    private UserRepository userRepo;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public UndergraduateProfileEntity getProfile(UserEntity user) {
        return profileRepo.findByUser_Id(user.getId()).orElse(null);
    }

    public void updateProfile(String email, String firstName, String lastName) {

        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(firstName);
        user.setLastName(lastName);

        userRepo.save(user);
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {

        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        // Validate new password
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Password must be at least 6 characters with uppercase and lowercase letters");
        }

        // Save new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        return true;
    }

    private boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= 6 &&
                password.matches(".*[A-Z].*") &&   // at least one uppercase
                password.matches(".*[a-z].*");     // at least one lowercase
    }




}