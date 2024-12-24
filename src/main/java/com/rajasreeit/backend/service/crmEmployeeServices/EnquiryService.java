package com.rajasreeit.backend.service.crmEmployeeServices;

import com.rajasreeit.backend.dto.EnquiryWithEmployeeName;
import com.rajasreeit.backend.entities.CrmEmployee;
import com.rajasreeit.backend.entities.crmEmployeeEntities.Enquiry;
import com.rajasreeit.backend.repo.CrmEmployeeRepo;
import com.rajasreeit.backend.repo.crmEmployeeRepos.EnquiryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnquiryService {

    @Autowired
    private EnquiryRepo enquiryRepo;

    @Autowired
    private CrmEmployeeRepo crmEmployeeRepo;

    /**
     * Add a new enquiry.
     */
    public Enquiry addEnquiry(Enquiry enquiry) {
        // Fetch authenticated user details
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Find the employee by mobile (username)
        Optional<CrmEmployee> employeeOpt = Optional.ofNullable(crmEmployeeRepo.findByMobile(username));
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }

        CrmEmployee employee = employeeOpt.get();
        enquiry.setCrmEmployee(employee);
        return enquiryRepo.save(enquiry);
    }

    /**
     * Get all enquiries for the authenticated employee.
     */
    public List<Enquiry> getEnquiriesForAuthenticatedEmployee() {
        // Fetch authenticated user details
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Find the employee by mobile (username)
        CrmEmployee employee = crmEmployeeRepo.findByMobile(username);
        if (employee == null) {
            throw new RuntimeException("Employee not found");
        }

        // Fetch all enquiries for the employee
        return enquiryRepo.findByCrmEmployee(employee);
    }


    /**
     * Get all enquiries with employee names.
     */
    public List<EnquiryWithEmployeeName> getEnquiriesWithEmployeeName() {
        // Fetch all enquiries
        List<Enquiry> enquiries = enquiryRepo.findAll();

        // Map to EnquiryWithEmployeeName objects
        return enquiries.stream().map(enquiry -> {
            EnquiryWithEmployeeName enquiryWithEmployeeName = new EnquiryWithEmployeeName();
            enquiryWithEmployeeName.setId(enquiry.getId());
            enquiryWithEmployeeName.setTitle(enquiry.getTitle());
            enquiryWithEmployeeName.setMessage(enquiry.getMessage());
            enquiryWithEmployeeName.setEmployeeName(enquiry.getCrmEmployee().getFullName());
            return enquiryWithEmployeeName;
        }).collect(Collectors.toList());
    }
}
