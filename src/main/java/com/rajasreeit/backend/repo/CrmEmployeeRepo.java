package com.rajasreeit.backend.repo;

import com.rajasreeit.backend.entities.CrmEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrmEmployeeRepo extends JpaRepository<CrmEmployee, Integer> {

    boolean existsByMobile(String mobileNumber);
    boolean existsByEmail(String email);

    CrmEmployee findByMobile(String mobile);
}
