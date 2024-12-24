package com.rajasreeit.backend.controller.crmControllers;

import com.rajasreeit.backend.dto.EnquiryWithEmployeeName;
import com.rajasreeit.backend.entities.crmEmployeeEntities.Enquiry;
import com.rajasreeit.backend.service.crmEmployeeServices.EnquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crm/employee")
public class EnquiryController {

    @Autowired
    private EnquiryService enquiryService;

    @PostMapping("/add-enquiry")
    public ResponseEntity<Enquiry> addEnquiry(@RequestBody Enquiry enquiry) {
        return ResponseEntity.ok(enquiryService.addEnquiry(enquiry));
    }


    @GetMapping("/enquiries")
    public ResponseEntity<List<Enquiry>> getEnquiries() {
        return ResponseEntity.ok(enquiryService.getEnquiriesForAuthenticatedEmployee());
    }



    @GetMapping("/enquiries-with-names")
    public ResponseEntity<List<EnquiryWithEmployeeName>> getEnquiriesWithEmployeeName() {
        return ResponseEntity.ok(enquiryService.getEnquiriesWithEmployeeName());
    }
}
