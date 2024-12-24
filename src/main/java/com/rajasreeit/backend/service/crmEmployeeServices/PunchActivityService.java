package com.rajasreeit.backend.service.crmEmployeeServices;

import com.rajasreeit.backend.dto.PunchActivityDTO;
import com.rajasreeit.backend.entities.CrmEmployee;
import com.rajasreeit.backend.entities.crmEmployeeEntities.CrmPunchActivity;

import com.rajasreeit.backend.filter.JwtFilter;
import com.rajasreeit.backend.repo.CrmEmployeeRepo;
import com.rajasreeit.backend.repo.crmEmployeeRepos.PunchActivityRepo;
import com.rajasreeit.backend.service.CrmDetailsService;
import com.rajasreeit.backend.service.CrmEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PunchActivityService {

    @Autowired
    private PunchActivityRepo punchActivityRepo;

    @Autowired
    private CrmEmployeeRepo crmEmployeeRepo;

    @Autowired
    private CrmDetailsService crmEmployeeService;

    @Autowired
    private JwtFilter jwtUtil;

    public CrmPunchActivity savePunchActivity(MultipartFile punchInImage, MultipartFile punchOutImage, String workReport, String remainderDate, String mobile) throws IOException {
        CrmEmployee crmEmployee = crmEmployeeRepo.findByMobile(mobile);
        if (crmEmployee == null) {
            throw new RuntimeException("Employee not found. Please check the mobile number.");
        }

        // Get current date
        String currentDate = LocalDate.now().toString();

        // Check if a record already exists for today's date
        Optional<CrmPunchActivity> existingPunchOpt = Optional.ofNullable(punchActivityRepo.findByCrmEmployeeAndDate(crmEmployee, currentDate));

        CrmPunchActivity punchActivity;

        if (existingPunchOpt.isPresent()) {
            // Update existing record
            punchActivity = existingPunchOpt.get();

            if (punchInImage != null) {
                if (punchActivity.getTimeOfPunchIn() != null) {
                    throw new IllegalStateException("Warning: You have already punched in today at " +
                            punchActivity.getTimeOfPunchIn() + ".");
                }
                punchActivity.setPunchInImage(punchInImage.getBytes());
                punchActivity.setTimeOfPunchIn(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }

            if (punchOutImage != null) {
                if (punchActivity.getTimeOfPunchIn() == null) {
                    throw new IllegalStateException("Warning: You cannot punch out before punching in. Please punch in first.");
                }
                if (punchActivity.getTimeOfPunchOut() != null) {
                    throw new IllegalStateException("Warning: You have already punched out today at " +
                            punchActivity.getTimeOfPunchOut() + ".");
                }

                if (workReport == null || workReport.trim().isEmpty()) {
                    throw new IllegalStateException("Work report is required before punching out.");
                }

                // Calculate time difference and include in the response
                LocalTime punchInTime = LocalTime.parse(punchActivity.getTimeOfPunchIn());
                LocalTime currentPunchOutTime = LocalTime.now();
                Duration duration = Duration.between(punchInTime, currentPunchOutTime);
                long workedHours = duration.toHours();
                long workedMinutes = duration.toMinutesPart();

                // Save punch-out details
                punchActivity.setPunchOutImage(punchOutImage.getBytes());
                punchActivity.setTimeOfPunchOut(currentPunchOutTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                punchActivity.setWorkReport(workReport);
                punchActivity.setRemainderDate(remainderDate);

                // Update the worked time
                punchActivity.setWorkedHours(String.format("%02d:%02d", workedHours, workedMinutes));
                punchActivity.setLoginTime(duration.getSeconds()); // Save login time in seconds
            }

        } else {
            // Create a new record for punch-in
            if (punchOutImage != null) {
                throw new IllegalStateException("Warning: Punch-out cannot be recorded before punch-in. Please punch in first.");
            }

            punchActivity = new CrmPunchActivity();
            punchActivity.setCrmEmployee(crmEmployee);
            punchActivity.setDate(currentDate);

            if (punchInImage != null) {
                punchActivity.setPunchInImage(punchInImage.getBytes());
                punchActivity.setTimeOfPunchIn(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        }

        // Save the punch activity
        return punchActivityRepo.save(punchActivity);
    }


    public List<CrmPunchActivity> getAllPunchActivities() {

        return punchActivityRepo.findAll();
    }

    public Optional<CrmPunchActivity> getPunchActivityById(int id) {

        return punchActivityRepo.findById(id);
    }

    public List<PunchActivityDTO> getPunchActivities() {
        // Get the authenticated user's details
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername(); // The authenticated user's username (e.g., mobile number)

        // Find the employee by the username (assumes username is the mobile number)
        CrmEmployee crmEmployee = crmEmployeeRepo.findByMobile(username);
        if (crmEmployee == null) {
            throw new RuntimeException("Employee not found for the authenticated user");
        }

        // Fetch punch activities associated with the employee
        List<CrmPunchActivity> punchActivities = punchActivityRepo.findByCrmEmployee(crmEmployee);
        if (punchActivities.isEmpty()) {
            throw new RuntimeException("No punch activities found for the authenticated employee");
        }

        // Map the CrmPunchActivity entities to PunchActivityDTO objects
        return punchActivities.stream()
                .map(punchActivity -> {
                    boolean punchInImagePresent = punchActivity.getPunchInImage() != null && punchActivity.getPunchInImage().length > 0;
                    boolean punchOutImagePresent = punchActivity.getPunchOutImage() != null && punchActivity.getPunchOutImage().length > 0;
                    String remainderDate = punchActivity.getRemainderDate();
                    String workReport = punchActivity.getWorkReport();
                    String punchInTime = punchActivity.getTimeOfPunchIn();
                    String punchOutTime = punchActivity.getTimeOfPunchOut();

                    // Return a PunchActivityDTO with image presence flags
                    return new PunchActivityDTO(
                            punchActivity.getDate(),
                            punchInImagePresent,
                            punchOutImagePresent,
                            remainderDate,
                            workReport,
                            punchInTime,
                            punchOutTime
                    );
                })
                .collect(Collectors.toList());
    }

}
