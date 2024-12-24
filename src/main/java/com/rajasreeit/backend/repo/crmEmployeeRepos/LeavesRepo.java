package com.rajasreeit.backend.repo.crmEmployeeRepos;

import com.rajasreeit.backend.entities.CrmEmployee;
import com.rajasreeit.backend.entities.crmEmployeeEntities.CrmLeaves;
import com.rajasreeit.backend.entities.crmEmployeeEntities.LeavesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeavesRepo extends JpaRepository<CrmLeaves, Integer> {

    List<CrmLeaves> findByCrmEmployee(CrmEmployee crmEmployee);

    List<CrmLeaves> findByLeavesEnum(LeavesEnum leavesEnum);
}
