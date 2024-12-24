package com.rajasreeit.backend.repo;

import com.rajasreeit.backend.entities.Holidays;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepo extends JpaRepository<Holidays, Integer> {
}
