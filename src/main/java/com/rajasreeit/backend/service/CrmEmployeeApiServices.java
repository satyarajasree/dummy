package com.rajasreeit.backend.service;


import com.rajasreeit.backend.repo.CrmEmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrmEmployeeApiServices {

    @Autowired
    private CrmEmployeeRepo crmEmployeeRepo;


}
