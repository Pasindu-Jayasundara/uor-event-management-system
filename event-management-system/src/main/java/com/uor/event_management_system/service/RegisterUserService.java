package com.uor.event_management_system.service;

import com.uor.event_management_system.dto.RegisterDto;
import com.uor.event_management_system.model.*;
import com.uor.event_management_system.repository.*;
import com.uor.event_management_system.util.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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




    public String registerUser(RegisterDto registerDto){

        if(userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            return "Already Registered in the System";
        }

        RoleEntity role = roleRepository.findRoleByRole(registerDto.getRole());

        System.out.println(registerDto);
        System.out.println(registerDto.getAccountType());

        Optional<AccountTypeEntity> accountType = accountTypeRepository.findById(Integer.parseInt(registerDto.getAccountType()));
        if(!accountType.isPresent()){
            return "Wrong Account Type";
        }

        UserEntity newUser = new UserEntity();
        newUser.setFirstName(registerDto.getFirstName());
        newUser.setLastName(registerDto.getLastName());
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setRole(role);
        newUser.setAccountType(accountType.get());

        boolean isAccountTypeSaved = false, isUserSaved = false;

        try{
            newUser = userRepository.save(newUser);
            isUserSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
            return "User Saving Failed";
        }

        if(accountType.get().getType().equalsIgnoreCase(AccountType.PROFILE_UNDERGRADUATE.name())){

            Optional<DepartmentEntity> department = departmentRepository.findById(Integer.parseInt(registerDto.getDepartment()));
            Optional<StudyYearEntity> studyYear = studyYearRepository.findById(Integer.parseInt(registerDto.getStudyYear()));

            if(department.isPresent() && studyYear.isPresent()){

                UndergraduateProfileEntity undergraduateProfileEntity = new UndergraduateProfileEntity();
                undergraduateProfileEntity.setUser(newUser);
                undergraduateProfileEntity.setDepartment(department.get());
                undergraduateProfileEntity.setStudyYear(studyYear.get());

                try{
                    undergraduateProfileRepository.save(undergraduateProfileEntity);
                    isAccountTypeSaved = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }else{

            StaffProfileEntity staffProfileEntity = new StaffProfileEntity();
            staffProfileEntity.setUser(newUser);

            try{
                staffProfileRepository.save(staffProfileEntity);
                isAccountTypeSaved = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(isAccountTypeSaved && isUserSaved){
            return "success";
        }else{
            return "failed";
        }
    }
}
