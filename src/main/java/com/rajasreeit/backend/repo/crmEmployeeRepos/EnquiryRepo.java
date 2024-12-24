package com.rajasreeit.backend.repo.crmEmployeeRepos;

import com.rajasreeit.backend.entities.CrmEmployee;
import com.rajasreeit.backend.entities.crmEmployeeEntities.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EnquiryRepo extends JpaRepository<Enquiry, Integer>{
    List<Enquiry> findByCrmEmployee(CrmEmployee crmEmployee);
}
