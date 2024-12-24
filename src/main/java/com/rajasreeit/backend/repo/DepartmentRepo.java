package com.rajasreeit.backend.repo;

import com.rajasreeit.backend.entities.Departments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepo extends JpaRepository<Departments, Integer> {
}
