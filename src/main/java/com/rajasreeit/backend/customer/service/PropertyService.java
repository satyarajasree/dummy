package com.rajasreeit.backend.customer.service;

import com.rajasreeit.backend.customer.entities.Properties;
import com.rajasreeit.backend.customer.repo.PropertyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {


    @Autowired
    private PropertyRepo propertyRepo;

    public Properties add(Properties properties){
        return propertyRepo.save(properties);
    }

    public List<Properties> getAllProperties(){
        return propertyRepo.findAll();
    }

    public Optional<Properties> getPropertyById(int id){
        return propertyRepo.findById(id);
    }
}
