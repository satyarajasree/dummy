package com.rajasreeit.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrmEmployeeEditDTO {

    @NotNull
    private String fullName;

    private String jobTitle;

    @Email
    private String email;

    private String mobile;

    private String address;

    private String days;

    private boolean isActive;

    // Add fields for file uploads
    private byte[] profileImage;
    private byte[] idCard;
}
