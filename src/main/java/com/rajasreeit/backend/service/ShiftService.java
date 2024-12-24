package com.rajasreeit.backend.service;


import com.rajasreeit.backend.entities.Shifts;
import com.rajasreeit.backend.repo.ShiftRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepo shiftRepo;


    public Shifts postShift(Shifts shifts){
        return shiftRepo.save(shifts);
    }

    public List<Shifts> findAllShifts(){
        return shiftRepo.findAll();
    }

    public Optional<Shifts> findShiftById(int id){
        return shiftRepo.findById(id);
    }

    public void deleteShift(int id){
        shiftRepo.deleteById(id);
    }

    public Shifts updateShift(int id, Shifts updatedShift){
        return shiftRepo.findById(id).map(shift -> {
            shift.setShiftName(updatedShift.getShiftName());
            shift.setEndTime(updatedShift.getEndTime());
            shift.setStartTime(updatedShift.getStartTime());
            return shiftRepo.save(shift);
        }).orElseThrow(()->new RuntimeException("Shift Not found"));
    }
}
