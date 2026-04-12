package com.uor.event_management_system.service;

import com.uor.event_management_system.dto.RegisterDto;
import com.uor.event_management_system.dto.RegisterUndergraduateDto;
import com.uor.event_management_system.model.*;
import com.uor.event_management_system.repository.*;
import com.uor.event_management_system.enums.AccountType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class RegisterUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private StudyYearRepository studyYearRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UndergraduateProfileRepository undergraduateProfileRepository;

    @Autowired
    private StaffProfileRepository staffProfileRepository;


    public HashMap<String, Optional<UserEntity>> registerUser(RegisterDto registerDto) {

        HashMap<String, Optional<UserEntity>> hashMap = new HashMap<>();

        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            hashMap.put("Already Registered in the System", Optional.empty());
            return hashMap;
        }

        RoleEntity role = roleRepository.findRoleByRole(registerDto.getRole());

        System.out.println(registerDto);
        System.out.println(registerDto.getAccountType());

        Optional<AccountTypeEntity> accountType = accountTypeRepository.findById(Integer.parseInt(registerDto.getAccountType()));
        if (!accountType.isPresent()) {
            hashMap.put("Wrong Account Type", Optional.empty());
            return hashMap;
        }

        UserEntity newUser = new UserEntity();
        newUser.setFirstName(registerDto.getFirstName());
        newUser.setLastName(registerDto.getLastName());
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setRole(role);
        newUser.setAccountType(accountType.get());
        newUser.setEnabled(1);


        try {
            UserEntity user = userRepository.save(newUser);
            hashMap.put("User saved", Optional.of(user));
            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
            hashMap.put("User Saving Failed", Optional.empty());
            return hashMap;
        }
    }

    public boolean registerUndergraduateUser(UserEntity user, RegisterUndergraduateDto registerUndergraduateDto) {

        Optional<DepartmentEntity> department = departmentRepository.findById(Integer.parseInt(registerUndergraduateDto.getDepartment()));
        Optional<StudyYearEntity> studyYear = studyYearRepository.findById(Integer.parseInt(registerUndergraduateDto.getStudyYear()));

        UndergraduateProfileEntity undergraduateProfileEntity = new UndergraduateProfileEntity();
        undergraduateProfileEntity.setUser(user);
        undergraduateProfileEntity.setDepartment(department.get());
        undergraduateProfileEntity.setStudyYear(studyYear.get());

        try {
            undergraduateProfileRepository.save(undergraduateProfileEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean registerStaffUser(UserEntity newUser) {

        StaffProfileEntity staffProfileEntity = new StaffProfileEntity();
        staffProfileEntity.setUser(newUser);
        staffProfileEntity.setVerified(0);

        try {
            staffProfileRepository.save(staffProfileEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
