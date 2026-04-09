package com.uor.event_management_system.service.admin;

import com.uor.event_management_system.dto.PlatformUserDTO;
import com.uor.event_management_system.dto.UserSummaryDto;
import com.uor.event_management_system.model.RoleEntity;
import com.uor.event_management_system.model.StaffProfileEntity;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.RoleRepository;
import com.uor.event_management_system.repository.StaffProfileRepository;
import com.uor.event_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StaffProfileRepository staffProfileRepository;

    @Autowired
    private RoleRepository roleRepository;



    public UserSummaryDto getPlatformUserSummary(){

        UserSummaryDto userSummaryDto = new UserSummaryDto();

        int totalUserCount = userRepository.countBy();
        int totalActiveUserCount = userRepository.countByEnabled(1);
        int totalInactiveUserCount = userRepository.countByEnabled(0);

        userSummaryDto.setTotalUsers(totalUserCount);
        userSummaryDto.setTotalActiveUsers(totalActiveUserCount);
        userSummaryDto.setTotalInactiveUsers(totalInactiveUserCount);

        return userSummaryDto;
    }

    public List<UserEntity> getStaffRegistrationRequestList() {
        return userRepository.getNotVerifiedStaffList();
    }

    public void approveStaffProfile(int id) {

        userRepository.findById(id).ifPresent(userEntity -> {

            StaffProfileEntity profile = userEntity.getStaffProfile();
            if (profile != null) {

                profile.setVerified(1);
                staffProfileRepository.save(profile);
            }
        });
    }

    public void rejectStaffProfile(int id) {

        userRepository.findById(id).ifPresent(userEntity -> {

            userEntity.setEnabled(0);
            userRepository.save(userEntity);
        });
    }

    public void enableUserProfile(int id) {

        userRepository.findById(id).ifPresent(userEntity -> {

            userEntity.setEnabled(1);
            userRepository.save(userEntity);
        });
    }

    public void disableProfile(int id) {

        userRepository.findById(id).ifPresent(userEntity -> {

            userEntity.setEnabled(0);
            userRepository.save(userEntity);
        });
    }

    public List<PlatformUserDTO> searchUsers(String q, String role, Boolean enabled) {
        return userRepository.findAll().stream()
                .filter(u -> q == null || u.getFirstName().toLowerCase().contains(q.toLowerCase()) || u.getLastName().toLowerCase().contains(q.toLowerCase())
                        || u.getEmail().toLowerCase().contains(q.toLowerCase()))
                .filter(u -> role == null || u.getRole().getRole().equalsIgnoreCase(role))
                .filter(u -> enabled == null || (u.getEnabled() == 1) == enabled)
                .map(PlatformUserDTO::from)
                .toList();
    }

    public PlatformUserDTO getUserById(int id) {
        return userRepository.findById(id)
                .map(PlatformUserDTO::from)
                .orElse(null);
    }

    public void changeUserRole(int id, String roleName) {
        userRepository.findById(id).ifPresent(userEntity -> {
            RoleEntity role = roleRepository.findByRole(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
            userEntity.setRole(role);
            userRepository.save(userEntity);
        });
    }

    public void setUserEnabled(int id, boolean enabled) {
        userRepository.findById(id).ifPresent(userEntity -> {
            userEntity.setEnabled(enabled ? 1 : 0);
            userRepository.save(userEntity);
        });
    }

    public void demoteUser(int id) {
        userRepository.findById(id).ifPresent(user -> {

            // Only demote if currently ADMIN
            if (!"ROLE_ADMIN".equalsIgnoreCase(user.getRole().getRole())) {
                return;
            }

            String accountType = user.getAccountType() != null ? user.getAccountType().getType() : "";

            String newRoleName;
            switch (accountType) {
                case "PROFILE_STAFF":
                    newRoleName = "ROLE_STAFF";
                    break;
                case "PROFILE_UNDERGRADUATE":
                    newRoleName = "ROLE_UNDERGRADUATE";
                    break;
                default:
                    newRoleName = "ROLE_USER"; // fallback
            }

            RoleEntity newRole = roleRepository.findByRole(newRoleName)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + newRoleName));

            user.setRole(newRole);
            userRepository.save(user);
        });
    }
}
