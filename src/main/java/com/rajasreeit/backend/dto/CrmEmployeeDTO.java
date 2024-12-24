package com.rajasreeit.backend.dto;

import com.rajasreeit.backend.entities.CrmEmployee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmEmployeeDTO {

    private int id;
    private String fullName;
    private String companyName;
    private String branchName;
    private String jobTitle;
    private String employeeId;
    private Integer shiftId; // Assuming you want to expose shiftId
    private String profileImagePath; // Base64 encoded image
    private String idCardPath; // Base64 encoded image
    private LocalDate dateOfJoining;
    private String email;
    private String mobile;
    private String address;
    private Integer departmentId;
    private boolean isActive;

    // Constructor to create DTO from CrmEmployee entity
    public CrmEmployeeDTO(CrmEmployee employee) {
        this.id = employee.getId();
        this.fullName = employee.getFullName();
        this.companyName = employee.getCompanyName();
        this.branchName = employee.getBranchName();
        this.jobTitle = employee.getJobTitle();
        this.employeeId = employee.getEmployeeId();
        this.shiftId = employee.getShifts() != null ? employee.getShifts().getId() : null; // Get shift ID
        this.profileImagePath = employee.getProfileImagePath();
        this.idCardPath = employee.getIdCardPath();
        this.dateOfJoining = employee.getDateOfJoining();
        this.email = employee.getEmail();
        this.mobile = employee.getMobile();
        this.address = employee.getAddress();
        this.departmentId = employee.getDepartments() != null ? employee.getDepartments().getId() : null;
        this.isActive = employee.isActive();
    }
}
