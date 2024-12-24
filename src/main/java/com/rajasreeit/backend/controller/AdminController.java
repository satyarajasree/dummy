package com.rajasreeit.backend.controller;

import com.rajasreeit.backend.dto.EnquiryWithEmployeeName;
import com.rajasreeit.backend.dto.PasswordChangeRequest;
import com.rajasreeit.backend.entities.CrmAdmin;
import com.rajasreeit.backend.entities.crmEmployeeEntities.CrmLeaves;
import com.rajasreeit.backend.entities.crmEmployeeEntities.CrmPunchActivity;
import com.rajasreeit.backend.entities.crmEmployeeEntities.LeaveWithEmployeeName;
import com.rajasreeit.backend.entities.crmEmployeeEntities.LeavesEnum;
import com.rajasreeit.backend.service.CrmAdminService;
import com.rajasreeit.backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/crm/admin")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8081"})
public class AdminController {

    @Autowired
    private CrmAdminService crmAdminService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/register")
    public ResponseEntity<CrmAdmin> registerAdmin(@RequestBody CrmAdmin crmAdmin) {
        try {
            CrmAdmin registeredAdmin = crmAdminService.register(crmAdmin);
            return new ResponseEntity<>(registeredAdmin, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody CrmAdmin crmAdmin) {
        try {
            String jwtToken = crmAdminService.verify(crmAdmin);
            if (jwtToken != null) {
                return new ResponseEntity<>(jwtToken, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during login", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            jwtService.blacklistToken(token);
            System.out.println("this is token"+token);
            return ResponseEntity.ok("Logout successful. Token invalidated.");
        }
        return ResponseEntity.badRequest().body("Invalid request. Token missing.");
    }

    // Endpoint to change the password
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody PasswordChangeRequest passwordChangeRequest,
            @RequestHeader("Authorization") String authHeader) {

        // Extract the token from the Authorization header (assuming Bearer token)
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;

        // Call the service method to change the password
        String response = crmAdminService.changePassword(
                passwordChangeRequest.getOldPassword(),
                passwordChangeRequest.getNewPassword(),
                passwordChangeRequest.getConfirmPassword(),
                token
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/leaves/{status}")
    public List<LeaveWithEmployeeName> getLeavesByStatus(@PathVariable LeavesEnum status) {
        return crmAdminService.getLeaves(status);
    }

    @PutMapping("/{leaveId}/status")
    public CrmLeaves updateLeavesEnum(@PathVariable int leaveId, @RequestParam LeavesEnum status) {
        return crmAdminService.updateLeavesEnum(leaveId, status);
    }


    @GetMapping("/punch/all")
    public ResponseEntity<List<CrmPunchActivity>> getAllPunchActivities() {
        List<CrmPunchActivity> punchActivities = crmAdminService.getAllPunchActivities();
        return ResponseEntity.ok(punchActivities);
    }


    @GetMapping("/punch/{id}")
    public ResponseEntity<CrmPunchActivity> getPunchActivityById(@PathVariable int id) {
        Optional<CrmPunchActivity> punchActivity = crmAdminService.getPunchActivityById(id);
        return punchActivity.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/punch/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<CrmPunchActivity> updatePunchActivity(
            @PathVariable int id,
            @RequestParam("date") String date,
            @RequestParam("timeOfPunchIn") String timeOfPunchIn,
            @RequestParam("timeOfPunchOut") String timeOfPunchOut,
            @RequestParam(value = "punchInImage", required = false) MultipartFile punchInImage,
            @RequestParam(value = "punchOutImage", required = false) MultipartFile punchOutImage) {

        try {
            CrmPunchActivity updatedActivity = crmAdminService.updatePunchActivity(
                    id, date, timeOfPunchIn, timeOfPunchOut, punchInImage, punchOutImage);
            return ResponseEntity.ok(updatedActivity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/enquiries")
    public ResponseEntity<List<EnquiryWithEmployeeName>> getAllEnquiries() {
        List<EnquiryWithEmployeeName> enquiries = crmAdminService.getAllEnquiries();
        return ResponseEntity.ok(enquiries);
    }

    @DeleteMapping("/enquiry/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable int id) {
        crmAdminService.deleteEnquiry(id);
        return ResponseEntity.noContent().build();
    }

}


