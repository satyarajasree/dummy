package com.rajasreeit.backend.customer.service;


import com.rajasreeit.backend.customer.entities.Customer;
import com.rajasreeit.backend.customer.entities.Employee;
import com.rajasreeit.backend.customer.repo.CustomerRepo;
import com.rajasreeit.backend.customer.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private EmployeeRepo employeeRepo;


    public String registerCustomer(
            String customerName,
            String fatherName,
            String dateOfBirth,
            int age,
            String aadharNumber,
            String mobileNumber,
            String email,
            String city,
            int pincode,
            String groupName,
            String panNumber,
            String primaryAddress,
            String nomineeName,
            String occupation,
            String employeeId,
            MultipartFile profileImage) {

        try {
            Customer customer = new Customer();
            customer.setCustomerName(customerName);
            customer.setFatherName(fatherName);
            customer.setDateOfBirth(LocalDate.parse(dateOfBirth));
            customer.setAge(age);
            customer.setAadharNumber(aadharNumber);
            customer.setMobileNumber(mobileNumber);
            customer.setEmail(email);
            customer.setCity(city);
            customer.setPincode(pincode);
            customer.setGroupName(groupName);
            customer.setPanNumber(panNumber);
            customer.setPrimaryAddress(primaryAddress);
            customer.setNomineeName(nomineeName);
            customer.setOccupation(occupation);

            // Set profile image if available
            if (profileImage != null && !profileImage.isEmpty()) {
                customer.setProfileImage(profileImage.getBytes());
            }

            // Retrieve and set employee reference
            Optional<Employee> employeeOpt = employeeRepo.findByEmployeeReferenceId(employeeId);
            if (employeeOpt.isPresent()) {
                customer.setEmployee(employeeOpt.get());
            } else {
                return "Employee not found";
            }

            customerRepo.save(customer);
            return "Customer registered successfully";

        } catch (IOException e) {
            return "Failed to save profile image";
        } catch (Exception e) {
            return "Failed to register customer";
        }
    }


    public String updateCustomerProfile(
            int customerId,
            String customerName,
            String email,
            String mobileNumber,
            String dateOfBirth,
            String primaryAddress,
            MultipartFile profileImage,
            String nomineeName) {

        Optional<Customer> customerOpt = customerRepo.findById(customerId);

        if (customerOpt.isEmpty()) {
            return "Customer not found";
        }

        Customer customer = customerOpt.get();

        // Update fields only if non-null values are provided
        if (customerName != null) customer.setCustomerName(customerName);
        if (email != null) customer.setEmail(email);
        if (mobileNumber != null) customer.setMobileNumber(mobileNumber);
        if (dateOfBirth != null) customer.setDateOfBirth(LocalDate.parse(dateOfBirth));
        if (primaryAddress != null) customer.setPrimaryAddress(primaryAddress);
        if (nomineeName != null) customer.setNomineeName(nomineeName);

        try {
            // Set profile image if provided and not empty
            if (profileImage != null && !profileImage.isEmpty()) {
                customer.setProfileImage(profileImage.getBytes());
            }
            customerRepo.save(customer);
            return "Customer profile updated successfully";
        } catch (IOException e) {
            return "Failed to update profile image";
        } catch (Exception e) {
            return "Failed to update customer profile";
        }
    }

}
