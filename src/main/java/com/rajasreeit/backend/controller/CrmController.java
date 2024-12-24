package com.rajasreeit.backend.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.rajasreeit.backend.dto.CrmEmployeeDTO;
import com.rajasreeit.backend.dto.CrmEmployeeEditDTO;
import com.rajasreeit.backend.entities.CrmEmployee;
import com.rajasreeit.backend.repo.CrmEmployeeRepo;
import com.rajasreeit.backend.service.CrmEmployeeService;
import com.rajasreeit.backend.service.JwtService;
import com.rajasreeit.backend.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/crm/admin/crm")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.1.21:8081/"})
public class CrmController {

    @Autowired
    private CrmEmployeeService crmEmployeeService;

    @Autowired
    private CrmEmployeeRepo crmEmployeeRepo;

    @Autowired
    private OtpService otpService;

    @Autowired
    private JwtService jwtService;

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Map<String, LocalDateTime> otpTimestamp = new HashMap<>();

    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(
            @RequestParam("fullName") String fullName,
            @RequestParam("companyName") String companyName,
            @RequestParam("branchName") String branchName,
            @RequestParam("jobTitle") String jobTitle,
            @RequestParam("employeeId") String employeeId,
            @RequestParam("dateOfJoining") String dateOfJoining,
            @RequestParam("email") String email,
            @RequestParam("mobile") String mobile,
            @RequestParam("address") String address,
            @RequestParam("departmentId") Integer departmentId,
            @RequestParam("shiftId") Integer shiftId,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam(value = "idCard", required = false) MultipartFile idCard,
            @RequestParam(value = "isActive", defaultValue = "true") boolean isActive) throws IOException {

        CrmEmployee employee = new CrmEmployee();
        employee.setFullName(fullName);
        employee.setCompanyName(companyName);
        employee.setBranchName(branchName);
        employee.setJobTitle(jobTitle);
        employee.setEmployeeId(employeeId);
        employee.setDateOfJoining(LocalDate.parse(dateOfJoining));
        employee.setEmail(email);
        employee.setMobile(mobile);
        employee.setAddress(address);
        employee.setActive(isActive);

        Optional<CrmEmployee> registeredEmployee = crmEmployeeService.registerEmployee(employee, profileImage, idCard, shiftId, departmentId);

        if (registeredEmployee.isEmpty()) {
            String message = crmEmployeeRepo.existsByMobile(employee.getMobile()) ?
                    "Mobile number already exists" : "Email already exists";
            return ResponseEntity.ok(message);
        }

        return new ResponseEntity<>(registeredEmployee.get(), HttpStatus.CREATED);
    }



    @GetMapping("/employees")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<CrmEmployeeDTO>> getAllCrmEmployees() {
        List<CrmEmployeeDTO> crmEmployees = crmEmployeeService.findAllCrmEmployees();
        return ResponseEntity.ok(crmEmployees);
    }


    @Value("${file.upload-dir}")
    private String uploadDir;

    // Endpoint to fetch the profile image
    @GetMapping("/images/{imageName}")
    public ResponseEntity<FileSystemResource> getImage(@PathVariable String imageName) {
        Path filePath = Paths.get(uploadDir, "images", imageName);

        FileSystemResource resource = new FileSystemResource(filePath);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resource);
    }

    // Endpoint to fetch the ID card image
    @GetMapping("/id-cards/{imageName}")
    public ResponseEntity<FileSystemResource> getIdCardImage(@PathVariable String imageName) {
        Path filePath = Paths.get(uploadDir, "id-cards", imageName);

        FileSystemResource resource = new FileSystemResource(filePath);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resource);
    }


    @GetMapping("/crm-employee/{id}")
    public ResponseEntity<CrmEmployee> getEmployeeById(@PathVariable int id) {
        CrmEmployee employee = crmEmployeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/edit-crm-employee/{id}")
    public ResponseEntity<CrmEmployee> updateEmployee(
            @PathVariable int id,
            @RequestParam(value = "fullName") String fullName,
            @RequestParam(value = "jobTitle") String jobTitle,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "mobile") String mobile,
            @RequestParam(value = "address") String address,
            @RequestParam(value = "isActive") boolean isActive) {

        CrmEmployeeEditDTO employeeEditDto = new CrmEmployeeEditDTO();
        employeeEditDto.setFullName(fullName);
        employeeEditDto.setJobTitle(jobTitle);
        employeeEditDto.setEmail(email);
        employeeEditDto.setMobile(mobile);
        employeeEditDto.setAddress(address);
        employeeEditDto.setActive(isActive);

        // Update the employee without handling profile images or ID cards
        CrmEmployee updatedEmployee = crmEmployeeService.updateEmployee(id, employeeEditDto);
        return ResponseEntity.ok(updatedEmployee);
    }


    @DeleteMapping("/delete-crm-employees/{id}")
    public ResponseEntity<Void> deleteCrmEmployee(@PathVariable int id){
        try {
            crmEmployeeService.deleteCrmEmployee(id);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String mobile) {
        CrmEmployee crmEmployee = crmEmployeeRepo.findByMobile(mobile);
        if (crmEmployee == null) {
            return "Employee not found";
        }
        if (!crmEmployee.isActive()) {
            return "Employee is not Active";
        } else {
            String otp = otpService.generateOTP();
            crmEmployee.setOtp(otp);
            crmEmployee.setOtpGeneratedTime(LocalDateTime.now());

            // Save the updated employee with OTP details
            crmEmployeeRepo.save(crmEmployee);

            // Send OTP via SMS
            sendOtpSms(mobile, otp);

            System.out.println("Generated OTP: " + otp);
        }

        return "OTP sent ";
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String mobile, @RequestParam String otp) {
        CrmEmployee crmEmployee = crmEmployeeRepo.findByMobile(mobile);
        if (crmEmployee == null) {
            return ResponseEntity.status(400).body("Employee not found!");
        }

        String storedOtp = crmEmployee.getOtp();
        LocalDateTime generatedTime = crmEmployee.getOtpGeneratedTime();

        if (storedOtp == null || generatedTime == null) {
            return ResponseEntity.status(400).body("OTP not sent or expired!");
        }

        if (otp.equals(storedOtp)) {
            if (generatedTime.plusMinutes(5).isAfter(LocalDateTime.now())) {
                // OTP is valid and not expired
                crmEmployee.setOtp(null);
                crmEmployee.setOtpGeneratedTime(null);
                crmEmployeeRepo.save(crmEmployee);

                String token = jwtService.generateToken(mobile, "EMPLOYEE");

                Map<String, Object> response = new HashMap<>();
                response.put("employee", crmEmployee);
                response.put("token", token);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(400).body("OTP has expired!");
            }
        } else {
            return ResponseEntity.status(400).body("Invalid OTP!");
        }
    }

    private void sendOtpSms(String mobile, String otp) {
        try {
            String username = "rajasreetownships";
            String apikey = "06440348c18ccff3316c";
            String senderid = "RJTOWN";  // Sender ID here
            // Replace the placeholder with the actual OTP
            String message = "Dear Customer, Please find your login OTP " + otp + ", for Rajasree Townships application. Please do not share it with anyone.";
            String templateid = "1707173442648245525";  // Correct Template ID here

            // URL encode the message
            String encodedMessage = URLEncoder.encode(message, "UTF-8");

            // Construct the URL for the API request
            String urlString = String.format(
                    "https://smslogin.co/v3/api.php?username=%s&apikey=%s&mobile=%s&senderid=%s&message=%s&templateid=%s",
                    username, apikey, mobile, senderid, encodedMessage, templateid);

            // Send the GET request
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print the response (Message ID or error message)
            System.out.println("SMS API Response: " + response.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
