package com.rajasreeit.backend.controller;


import com.rajasreeit.backend.entities.Shifts;
import com.rajasreeit.backend.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/crm/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;


    @PostMapping("/post-shift")
    public ResponseEntity<Shifts> postShift(@RequestBody Shifts shifts){
        Shifts shifts1 = shiftService.postShift(shifts);
        return ResponseEntity.status(201).body(shifts1);
    }

    @GetMapping("/shifts")
    public ResponseEntity<List<Shifts>> getAllShifts(){
        List<Shifts> shifts = shiftService.findAllShifts();
        return ResponseEntity.ok(shifts);
    }

    @GetMapping("/shifts/{id}")
    public ResponseEntity<Shifts> getShiftById(@PathVariable int id) {
        Optional<Shifts> shift = shiftService.findShiftById(id);
        if (shift.isPresent()) {
            return new ResponseEntity<>(shift.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/shift/{id}")
    public ResponseEntity<Shifts> updatedShift(@PathVariable int id, @RequestBody Shifts shifts){
        try {
            Shifts shifts1 = shiftService.updateShift(id, shifts);
            return ResponseEntity.ok(shifts1);
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/shift/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable int id){
        try {
            shiftService.deleteShift(id);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

}
