package com.rajasreeit.backend.controller.crmControllers;

import com.rajasreeit.backend.dto.PunchActivityDTO;
import com.rajasreeit.backend.entities.crmEmployeeEntities.CrmPunchActivity;
import com.rajasreeit.backend.service.JwtService;
import com.rajasreeit.backend.service.crmEmployeeServices.PunchActivityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("crm/employee")
public class PunchActivityController {

    @Autowired
    private PunchActivityService punchActivityService;

    @Autowired
    private JwtService jwtUtils;

    @PostMapping("punch")
    public ResponseEntity<?> savePunchActivity(
            @RequestParam(value = "punchInImage", required = false) MultipartFile punchInImage,
            @RequestParam(value = "punchOutImage", required = false) MultipartFile punchOutImage,
            @RequestParam(value = "workReport", required = false) String workReport,
            @RequestParam(value = "remainderDate", required = false) String remainderDate,
            HttpServletRequest request) {

        try {
            // Extract the mobile number from the JWT token in Authorization header
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new RuntimeException("JWT Token is missing or invalid");
            }

            String token = authorizationHeader.substring(7);
            String mobileNumber = jwtUtils.extractUsername(token); // You should implement jwtUtils to extract username

            // Call service to handle punch activity logic and calculate worked hours
            CrmPunchActivity savedPunchActivity = punchActivityService.savePunchActivity(punchInImage, punchOutImage, workReport, remainderDate ,mobileNumber);

            // If the punch activity was saved successfully, return the saved entity
            return ResponseEntity.ok(savedPunchActivity);

        } catch (IllegalStateException e) {
            // Handle validation errors (e.g., punching in/out issues)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/punch/all")
    public ResponseEntity<List<CrmPunchActivity>> getAllPunchActivities() {
        List<CrmPunchActivity> punchActivities = punchActivityService.getAllPunchActivities();
        return ResponseEntity.ok(punchActivities);
    }


    @GetMapping("/punch/{id}")
    public ResponseEntity<CrmPunchActivity> getPunchActivityById(@PathVariable int id) {
        Optional<CrmPunchActivity> punchActivity = punchActivityService.getPunchActivityById(id);
        return punchActivity.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/punch-activities")
    public ResponseEntity<List<PunchActivityDTO>> getPunchActivities() {
        try {
            // Fetch punch activities for the authenticated user
            List<PunchActivityDTO> punchActivities = punchActivityService.getPunchActivities();
            return ResponseEntity.ok(punchActivities);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



}
