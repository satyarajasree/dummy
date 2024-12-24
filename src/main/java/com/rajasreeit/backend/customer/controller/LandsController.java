package com.rajasreeit.backend.customer.controller;


import com.rajasreeit.backend.customer.entities.Lands;
import com.rajasreeit.backend.customer.service.LandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer/reference")
public class LandsController {

    @Autowired
    private LandService landService;


    @PostMapping("/add-lands")
    public ResponseEntity<Lands> addLand(@RequestBody Lands lands) {
        Lands savedLand = landService.add(lands);
        return new ResponseEntity<>(savedLand, HttpStatus.CREATED);
    }

    @GetMapping("/get-lands")
    public ResponseEntity<List<Lands>> getAllLands() {
        List<Lands> lands = landService.getAll();
        return new ResponseEntity<>(lands, HttpStatus.OK);
    }

    @GetMapping("/land/{id}")
    public ResponseEntity<Lands> getLandById(@PathVariable int id) {
        Optional<Lands> land = landService.getLandById(id);
        if (land.isPresent()) {
            return new ResponseEntity<>(land.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
