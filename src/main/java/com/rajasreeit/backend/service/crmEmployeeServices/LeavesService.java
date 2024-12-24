package com.rajasreeit.backend.service.crmEmployeeServices;

import com.rajasreeit.backend.entities.CrmEmployee;
import com.rajasreeit.backend.entities.crmEmployeeEntities.CrmLeaves;
import com.rajasreeit.backend.entities.crmEmployeeEntities.LeaveWithEmployeeName;
import com.rajasreeit.backend.repo.CrmEmployeeRepo;
import com.rajasreeit.backend.repo.crmEmployeeRepos.LeavesRepo;
import com.twilio.twiml.voice.Leave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeavesService {

    @Autowired
    private LeavesRepo leavesRepo;

    @Autowired
    private CrmEmployeeRepo crmEmployeeRepo;



    public CrmLeaves addLeaveRequest(CrmLeaves leaveRequest) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();


        Optional<CrmEmployee> employeeOpt = Optional.ofNullable(crmEmployeeRepo.findByMobile(username));

        if (employeeOpt.isPresent()) {
            CrmEmployee employee = employeeOpt.get();
            leaveRequest.setCrmEmployee(employee);
            leaveRequest.setName(employee.getFullName());
            return leavesRepo.save(leaveRequest);
        } else {
            throw new RuntimeException("Employee not found");
        }
    }

    public List<CrmLeaves> getAll(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        CrmEmployee employee = crmEmployeeRepo.findByMobile(username);

        if (employee == null) {
            throw new RuntimeException("Employee not found");
        }

        return leavesRepo.findByCrmEmployee(employee);
    }


    public List<LeaveWithEmployeeName> getLeavesWithEmployeeName() {
        // Fetch authenticated user details
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Find the employee by mobile (username)
        CrmEmployee employee = crmEmployeeRepo.findByMobile(username);
        if (employee == null) {
            throw new RuntimeException("Employee not found");
        }

        // Fetch leaves and map to LeaveWithEmployeeName
        return leavesRepo.findByCrmEmployee(employee).stream().map(leave -> {
            LeaveWithEmployeeName leaveWithEmployeeName = new LeaveWithEmployeeName();
            leaveWithEmployeeName.setStartDate(leave.getStartDate());
            leaveWithEmployeeName.setEndDate(leave.getEndDate());
            leaveWithEmployeeName.setReason(leave.getReason());
            leaveWithEmployeeName.setLeavesEnum(leave.getLeavesEnum());
            leaveWithEmployeeName.setEmployeeName(employee.getFullName());
            leaveWithEmployeeName.setLeaveDay(leave.getLeaveDay());
            leaveWithEmployeeName.setLeaveType(leave.getLeaveType());
            return leaveWithEmployeeName;
        }).collect(Collectors.toList());
    }
}
