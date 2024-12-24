package com.rajasreeit.backend.service;

import com.rajasreeit.backend.entities.Departments;
import com.rajasreeit.backend.entities.Holidays;
import com.rajasreeit.backend.repo.DepartmentRepo;
import com.rajasreeit.backend.repo.HolidayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class HolidayService {

    private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);

    @Autowired
    private HolidayRepo holidayRepo;

    @Autowired
    private DepartmentRepo departmentRepo;

    public Holidays addHoliday(LocalDate holidayDate, String reasonForHoliday, Integer departmentId) {
        Holidays holiday = new Holidays();
        holiday.setHolidayDate(holidayDate);
        holiday.setReasonForHoliday(reasonForHoliday);

        // Link to department
        Optional<Departments> department = departmentRepo.findById(departmentId);

        if (department.isPresent()) {
            holiday.setDepartments(department.get());
        } else {
            // Optionally log that the department was not found
            System.out.println("Department with ID " + departmentId + " not found. Holiday created without department link.");
        }

        return holidayRepo.save(holiday);
    }

    public List<Holidays> findAllHolidays() {
        logger.info("Retrieving all holidays");
        return holidayRepo.findAll();
    }

    public Optional<Holidays> getHoliday(int id) {
        logger.info("Fetching holiday with id: {}", id);
        return holidayRepo.findById(id);
    }

    public void deleteHoliday(int id) {
        logger.info("Deleting holiday with id: {}", id);
        holidayRepo.deleteById(id);
    }

    // Method to update holiday with department changes
    public Holidays updateHoliday(int id, Holidays updatedHoliday) {

        return holidayRepo.findById(id).map(existingHoliday -> {
            // Update fields of the existing holiday
            existingHoliday.setHolidayDate(updatedHoliday.getHolidayDate());
            existingHoliday.setReasonForHoliday(updatedHoliday.getReasonForHoliday());

            // Update department list if it's provided in updatedHoliday
            if (updatedHoliday.getDepartments() != null) {
                // Here we update the department entities as per the updatedHoliday input
                existingHoliday.setDepartments(updatedHoliday.getDepartments());
            }

            // Save the updated holiday entity
            return holidayRepo.save(existingHoliday);
        }).orElseThrow(() -> new RuntimeException("Holiday not found with id " + id));
    }

    public Optional<Holidays> getHolidayById(int id) {
        logger.info("Fetching holiday by id: {}", id);
        return holidayRepo.findById(id);
    }
}
