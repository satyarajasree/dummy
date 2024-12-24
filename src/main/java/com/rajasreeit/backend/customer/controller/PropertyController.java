package com.rajasreeit.backend.customer.controller;


import com.rajasreeit.backend.customer.entities.Properties;
import com.rajasreeit.backend.customer.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/customer/reference")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/add-property")
    public ResponseEntity<Properties> addProperty(@RequestBody Properties properties) {
        Properties savedProperty = propertyService.add(properties);
        return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);
    }

    @GetMapping("/get-properties")
    public ResponseEntity<List<Properties>> getAllProperties() {
        List<Properties> properties = propertyService.getAllProperties();
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    @GetMapping("/property/{id}")
    public ResponseEntity<Properties> getPropertyById(@PathVariable int id) {
        Optional<Properties> property = propertyService.getPropertyById(id);
        if (property.isPresent()) {
            return new ResponseEntity<>(property.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
