package com.rajasreeit.backend.customer.service;


import com.rajasreeit.backend.customer.entities.Lands;
import com.rajasreeit.backend.customer.entities.Properties;
import com.rajasreeit.backend.customer.repo.LandsRepo;
import com.rajasreeit.backend.customer.repo.PropertyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LandService {

    @Autowired
    private LandsRepo landsRepo;

    @Autowired
    private PropertyRepo propertyRepo;

    // Method to add a new Land with only property_id
    public Lands add(Lands lands) {
        // Check if the property_id in lands is null or not set
        if (lands.getProperties() == null || lands.getProperties().getId() == 0) {
            throw new RuntimeException("Property ID must be provided.");
        }

        // Fetch the Properties object by its id from the property_repo
        Properties property = propertyRepo.findById(lands.getProperties().getId())
                .orElseThrow(() -> new RuntimeException("Property with ID " + lands.getProperties().getId() + " not found"));

        // Set the fetched property to the lands entity
        lands.setProperties(property);

        // Save and return the Land entity
        return landsRepo.save(lands);
    }

    public List<Lands> getAll(){
        return landsRepo.findAll();
    }

    public Optional<Lands> getLandById(int id){
        return landsRepo.findById(id);
    }
}
