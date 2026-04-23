package com.uor.event_management_system.service;

import com.uor.event_management_system.model.OrganizeBy;
import com.uor.event_management_system.model.UserEntity;
import com.uor.event_management_system.repository.OrganizeByRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static aQute.bnd.annotation.headers.Category.users;

@Service
public class OrganizeByService {

    @Autowired
    private OrganizeByRepository organizeByRepository;

    public List<UserEntity> getOrganizers(int eventId) {

        List<OrganizeBy> list = organizeByRepository.findByEvent_Id(eventId);

        Set<UserEntity> uniqueUsers = new HashSet<>();

        for (OrganizeBy ob : list) {
            uniqueUsers.add(ob.getUser());
        }

        return new ArrayList<>(uniqueUsers);
    }


}
