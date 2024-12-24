package com.rajasreeit.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrmEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fullName;

    private String companyName;

    private String branchName;

    private String jobTitle;

    private String employeeId;

    @ManyToOne
    private Shifts shifts;

    @Column(name = "profile_image")
    private String profileImagePath;

    @Column(name = "id_card")
    private String idCardPath;

    private LocalDate dateOfJoining;

    @Email
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobile;

    private String address;

    @ManyToOne()
    private Departments departments;

    private boolean isActive;

    // New fields for OTP and timestamp
    private String otp;
    private LocalDateTime otpGeneratedTime;
}
