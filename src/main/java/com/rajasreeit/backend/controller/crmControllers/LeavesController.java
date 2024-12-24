package com.rajasreeit.backend.controller.crmControllers;

import com.rajasreeit.backend.entities.crmEmployeeEntities.CrmLeaves;
import com.rajasreeit.backend.entities.crmEmployeeEntities.LeaveWithEmployeeName;
import com.rajasreeit.backend.service.crmEmployeeServices.LeavesService;
import com.twilio.twiml.voice.Leave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("crm/employee")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.1.21:8081/"})
public class LeavesController {

    @Autowired
    private LeavesService leavesService;

    // Endpoint to create a leave request
    @PostMapping("/add-leave")
    public ResponseEntity<CrmLeaves> addLeaveRequest(@RequestBody CrmLeaves leaveRequest) {
        try {
            CrmLeaves createdLeave = leavesService.addLeaveRequest(leaveRequest);
            return new ResponseEntity<>(createdLeave, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/get-leaves")
    public ResponseEntity<List<CrmLeaves>> getAllLeaves(){
        List<CrmLeaves> crmLeaves = leavesService.getAll();
        return ResponseEntity.ok(crmLeaves);
    }

    @GetMapping("/with-employee-name")
    public ResponseEntity<List<LeaveWithEmployeeName>> getLeavesWithEmployeeName() {
        List<LeaveWithEmployeeName> leaves = leavesService.getLeavesWithEmployeeName();
        return ResponseEntity.ok(leaves);
    }

}
