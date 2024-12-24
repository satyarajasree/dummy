package com.rajasreeit.backend.repo.crmEmployeeRepos;

import com.rajasreeit.backend.entities.CrmEmployee;
import com.rajasreeit.backend.entities.crmEmployeeEntities.CrmPunchActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PunchActivityRepo extends JpaRepository<CrmPunchActivity, Integer> {
    CrmPunchActivity findByCrmEmployeeAndDate(CrmEmployee crmEmployee, String date);
    Optional<CrmPunchActivity> findByCrmEmployee_MobileAndDate(String mobile, String date);
    List<CrmPunchActivity> findByCrmEmployee(CrmEmployee crmEmployee);

}
