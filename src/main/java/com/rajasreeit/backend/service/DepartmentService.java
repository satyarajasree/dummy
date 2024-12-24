package com.rajasreeit.backend.service;

import com.rajasreeit.backend.entities.Departments;

import com.rajasreeit.backend.repo.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepo departmentRepo;

    // Add a new department
    public Departments addDepartment(Departments department) {
        return departmentRepo.save(department);
    }

    // Get a department by ID
    public Optional<Departments> getDepartmentById(int id) {
        return departmentRepo.findById(id);
    }

    // Get all departments
    public List<Departments> getAllDepartments() {
        return departmentRepo.findAll();
    }

    // Update a department
    public Departments updateDepartment(int id, Departments updatedDepartment) {
        return departmentRepo.findById(id)
                .map(department -> {
                    department.setDepartment(updatedDepartment.getDepartment());
                    department.setDepartmentDescription(updatedDepartment.getDepartmentDescription());

                    return departmentRepo.save(department);
                }).orElseThrow(() -> new RuntimeException("Department not found with id " + id));
    }

    // Delete a department by ID
    public void deleteDepartment(int id) {
        departmentRepo.deleteById(id);
    }
}
