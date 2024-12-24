package com.rajasreeit.backend.customer.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empId;

    @NotNull(message = "Employee name is required")
    @Column(name = "employee_name")
    private String employeeName;

    @NotNull(message = "Designation is required")
    @Column(name = "designation")
    private String designation;

    @NotNull(message = "Department is required")
    @Column(name = "department")
    private String department;

    @Column(name = "employee_reference_id", unique = true, nullable = false)
    private String employeeReferenceId;

    @PrePersist
    private void generateEmployeeReferenceId() {
        // Generate a unique reference ID
        this.employeeReferenceId = "EMP" + System.currentTimeMillis(); // Example using current timestamp
    }
}
