package com.rajasreeit.backend.customer.controller;


import com.rajasreeit.backend.customer.entities.Customer;
import com.rajasreeit.backend.customer.repo.CustomerRepo;
import com.rajasreeit.backend.customer.service.CustomerService;
import com.rajasreeit.backend.service.JwtService;
import com.rajasreeit.backend.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private JwtService jwtService;

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Map<String, LocalDateTime> otpTimestamp = new HashMap<>();


    //Registering customer with or with out profile picture
    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(
            @RequestParam("customerName") String customerName,
            @RequestParam("fatherName") String fatherName,
            @RequestParam("dateOfBirth") String dateOfBirth,
            @RequestParam("age") int age,
            @RequestParam("aadharNumber") String aadharNumber,
            @RequestParam("mobileNumber") String mobileNumber,
            @RequestParam("email") String email,
            @RequestParam("city") String city,
            @RequestParam("pincode") int pincode,
            @RequestParam("groupName") String groupName,
            @RequestParam("panNumber") String panNumber,
            @RequestParam("primaryAddress") String primaryAddress,
            @RequestParam("nomineeName") String nomineeName,
            @RequestParam("occupation") String occupation,
            @RequestParam("employeeId") String employeeId,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {

        String result = customerService.registerCustomer(
                customerName, fatherName, dateOfBirth, age, aadharNumber, mobileNumber,
                email, city, pincode, groupName, panNumber, primaryAddress, nomineeName,
                occupation, employeeId, profileImage);

        if (result.equals("Customer registered successfully")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/login")
    public String login(@RequestParam String mobileNumber) {
        Customer customer = customerRepo.findByMobileNumber(mobileNumber);
        if (customer == null) {
            return "Customer not found";
        }

        // Generate OTP
        String otp = otpService.generateOTP();
        customer.setOtp(otp);
        customer.setOtpGeneratedTime(LocalDateTime.now());

        // Save the updated customer with OTP details
        customerRepo.save(customer);
        sendOtpSms(mobileNumber, otp);
        System.out.println("Generated OTP: " + otp);

        // Optionally send OTP to email
        if (customer.getEmail() != null) {
            otpService.sendSimpleEmail(customer.getEmail(), "Your OTP is", otp);
        }

        return "OTP sent to " + mobileNumber + (customer.getEmail() != null ? " and " + customer.getEmail() : "");
    }



    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String mobileNumber, @RequestParam String otp) {
        Customer customer = customerRepo.findByMobileNumber(mobileNumber);
        if (customer == null) {
            return ResponseEntity.status(400).body("Customer not found!");
        }

        // Retrieve stored OTP and timestamp from the database
        String storedOtp = customer.getOtp();
        LocalDateTime generatedTime = customer.getOtpGeneratedTime();

        if (storedOtp == null || generatedTime == null) {
            return ResponseEntity.status(400).body("OTP not sent or expired!");
        }

        if (otp.equals(storedOtp)) {
            if (generatedTime.plusMinutes(5).isAfter(LocalDateTime.now())) {
                // OTP is valid and not expired
                customer.setOtp(null);
                customer.setOtpGeneratedTime(null);
                customerRepo.save(customer);

                // Generate JWT token
                String token = jwtService.generateToken(mobileNumber, "CUSTOMER");

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("customer", customer);

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

    //Customer profile update
    @PutMapping("/{customerId}/updateProfile")
    public ResponseEntity<String> updateCustomerProfile(
            @PathVariable("customerId") int customerId,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "mobileNumber", required = false) String mobileNumber,
            @RequestParam(value = "dateOfBirth", required = false) String dateOfBirth,
            @RequestParam(value = "primaryAddress", required = false) String primaryAddress,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam(value = "nomineeName", required = false) String nomineeName) {

        String result = customerService.updateCustomerProfile(
                customerId, customerName, email, mobileNumber, dateOfBirth,
                primaryAddress, profileImage, nomineeName);

        if (result.equals("Customer profile updated successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }
}
