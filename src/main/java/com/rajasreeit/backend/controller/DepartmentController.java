package com.rajasreeit.backend.controller;

import com.rajasreeit.backend.entities.Departments;

import com.rajasreeit.backend.service.DepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crm/admin")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // Create a new department
    @PostMapping("/add-department")
    public ResponseEntity<Departments> addDepartment(@RequestBody Departments department) {
        return ResponseEntity.ok(departmentService.addDepartment(department));
    }

    // Get a department by ID
    @GetMapping("/departments/{id}")
    public ResponseEntity<Departments> getDepartmentById(@PathVariable int id) {
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all departments
    @GetMapping("/departments")
    public List<Departments> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }

    // Update a department
    @PutMapping("/departments/{id}")
    public ResponseEntity<Departments> updateDepartment(@PathVariable int id, @RequestBody Departments department) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, department));
    }

    // Delete a department
    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
