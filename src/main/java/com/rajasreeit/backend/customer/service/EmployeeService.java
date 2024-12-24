package com.rajasreeit.backend.customer.service;

import com.rajasreeit.backend.customer.entities.Employee;
import com.rajasreeit.backend.exceptions.EmployeeNotFoundException;
import com.rajasreeit.backend.customer.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    public Employee findEmployeeById(int empId) {
        Optional<Employee> employee = employeeRepo.findById(empId);
        return employee.orElse(null);
    }

    public List<Employee> findAllEmployees() {
        return employeeRepo.findAll();
    }

    public Employee findEmployeeByReferenceId(String referenceId) {
        return employeeRepo.findByEmployeeReferenceId(referenceId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with reference ID " + referenceId + " not found"));
    }
    

}
