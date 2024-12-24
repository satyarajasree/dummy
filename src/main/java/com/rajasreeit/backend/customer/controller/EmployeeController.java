package com.rajasreeit.backend.customer.controller;


import com.rajasreeit.backend.customer.entities.Employee;
import com.rajasreeit.backend.exceptions.EmployeeNotFoundException;
import com.rajasreeit.backend.customer.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/reference")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.findAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employee/{referenceId}")
    public ResponseEntity<?> getEmployeeByReferenceId(@PathVariable String referenceId) {
        try {
            Employee employee = employeeService.findEmployeeByReferenceId(referenceId);
            return ResponseEntity.ok(employee);
        } catch (EmployeeNotFoundException ex) {
            return ResponseEntity.ok(ex.getMessage());
        }
    }

}
