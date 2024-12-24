package com.rajasreeit.backend.customer.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "guardian_name")
    private String fatherName;

    @Past(message = "Date of Birth should be in the past")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Min(value = 0, message = "Age must be a positive number")
    @Max(value = 150, message = "Age must be less than or equal to 150")
    @Column(name = "age")
    private int age;

    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhar number must be 12 digits")
    @Column(name = "aadhar_number", unique = true)
    private String aadharNumber;

    @Pattern(regexp = "^\\+91[789]\\d{9}$", message = "Mobile number must be a valid Indian number in the format +91XXXXXXXXXX, where X is a digit")
    @Column(name = "mobile_number", unique = true)
    private String mobileNumber;

    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;

    @Column(name = "city")
    private String city;

    @Min(value = 100000, message = "Pincode must be at least 6 digits")
    @Max(value = 999999, message = "Pincode must be at most 6 digits")
    @Column(name = "pincode")
    private int pincode;

    @Column(name = "group_name")
    private String groupName;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "PAN number must be valid")
    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "primary_address")
    private String primaryAddress;

    @Column(name = "nominee_name")
    private String nomineeName;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "profile_image", columnDefinition = "LONGBLOB", nullable = true)
    private byte[] profileImage;

    @ManyToOne
    @JoinColumn(name = "employee_reference_id", referencedColumnName = "employee_reference_id", nullable = false)
    private Employee employee;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_generated_time")
    private LocalDateTime otpGeneratedTime;

}
