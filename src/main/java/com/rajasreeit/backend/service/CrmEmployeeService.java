package com.rajasreeit.backend.service;


import com.rajasreeit.backend.dto.CrmEmployeeDTO;
import com.rajasreeit.backend.dto.CrmEmployeeEditDTO;
import com.rajasreeit.backend.entities.CrmAdmin;
import com.rajasreeit.backend.entities.CrmEmployee;
import com.rajasreeit.backend.entities.Departments;
import com.rajasreeit.backend.entities.Shifts;
import com.rajasreeit.backend.imagesS3.S3FileUploadService;
import com.rajasreeit.backend.repo.CrmEmployeeRepo;
import com.rajasreeit.backend.repo.DepartmentRepo;
import com.rajasreeit.backend.repo.ShiftRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CrmEmployeeService {

    @Autowired
    private CrmEmployeeRepo crmEmployeeRepo;

    @Autowired
    private ShiftRepo shiftRepo;

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;




    @Value("${file.upload-dir}")
    private String uploadDir;  // Dynamic directory for file uploads

    @Autowired
    private S3FileUploadService s3FileUploadService;

    public Optional<CrmEmployee> registerEmployee(
            CrmEmployee employee,
            MultipartFile profileImage,
            MultipartFile idCard,
            Integer shiftId,
            Integer departmentId) throws IOException {

        // Check for duplicate mobile or email
        if (crmEmployeeRepo.existsByMobile(employee.getMobile()) || crmEmployeeRepo.existsByEmail(employee.getEmail())) {
            return Optional.empty();
        }

        // Assign shift and department
        if (shiftId != null) {
            shiftRepo.findById(shiftId).ifPresent(employee::setShifts);
        }
        if (departmentId != null) {
            departmentRepo.findById(departmentId).ifPresent(employee::setDepartments);
        }

        // Handle profile image upload
        if (profileImage != null && !profileImage.isEmpty()) {
            String profileImagePath = s3FileUploadService.uploadFile(
                    "images", profileImage, "profile_" + employee.getMobile());
            employee.setProfileImagePath(profileImagePath);
        }

        // Handle ID card image upload
        if (idCard != null && !idCard.isEmpty()) {
            String idCardPath = s3FileUploadService.uploadFile(
                    "id-cards", idCard, "idcard_" + employee.getMobile());
            employee.setIdCardPath(idCardPath);
        }

        // Save employee
        CrmEmployee savedEmployee = crmEmployeeRepo.save(employee);
        return Optional.of(savedEmployee);
    }


    // Utility method to save file and return path
    private String saveFile(String subDir, MultipartFile file, String fileName) throws IOException {
        // Combine base upload directory with sub-directory
        Path directoryPath = Paths.get(uploadDir, subDir);

        // Create directories if they don't exist
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // Extract file extension
        String fileExtension = Optional.ofNullable(file.getOriginalFilename())
                .filter(f -> f.contains("."))
                .map(f -> f.substring(file.getOriginalFilename().lastIndexOf(".")))
                .orElse("");

        // Construct file path
        Path filePath = directoryPath.resolve(fileName + fileExtension);

        // Save file to the path
        Files.write(filePath, file.getBytes());

        // Return the relative path (adjusted for how you serve the files in your application)
        return filePath.toString().replace("\\", "/").replace(uploadDir, "");
    }


    // Get all employees
    public List<CrmEmployeeDTO> findAllCrmEmployees() {
        List<CrmEmployee> employees = crmEmployeeRepo.findAll();
        return employees.stream()
                .map(CrmEmployeeDTO::new)
                .collect(Collectors.toList());
    }

    public CrmEmployee getEmployeeById(int id) {
        return crmEmployeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }


    public CrmEmployee updateEmployee(int id, CrmEmployeeEditDTO employeeEditDto) {
        CrmEmployee employee = crmEmployeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Update only the fields from the DTO
        employee.setFullName(employeeEditDto.getFullName());
        employee.setJobTitle(employeeEditDto.getJobTitle());
        employee.setEmail(employeeEditDto.getEmail());
        employee.setMobile(employeeEditDto.getMobile());
        employee.setAddress(employeeEditDto.getAddress());
        employee.setActive(employeeEditDto.isActive());

        return crmEmployeeRepo.save(employee);
    }


    public void deleteCrmEmployee(int id){
        crmEmployeeRepo.deleteById(id);
    }




}
