package com.uor.event_management_system.service;


import com.uor.event_management_system.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Autowired
    FilesRepository  filesRepository;


    public int getFileCount(Integer event_id) {
        return filesRepository.countByEvent_Id(event_id);
    }


}
