package com.rajasreeit.backend.repo;

import com.rajasreeit.backend.entities.Shifts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepo extends JpaRepository<Shifts, Integer> {

}
