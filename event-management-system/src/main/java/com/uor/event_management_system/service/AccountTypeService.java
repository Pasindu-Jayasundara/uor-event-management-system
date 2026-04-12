package com.uor.event_management_system.service;

import com.uor.event_management_system.model.AccountTypeEntity;
import com.uor.event_management_system.repository.AccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountTypeService {

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    public List<AccountTypeEntity> getAllAccountTypes(){
        return accountTypeRepository.findAll();
    }
}
