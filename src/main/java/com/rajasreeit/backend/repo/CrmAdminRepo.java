package com.rajasreeit.backend.repo;

import com.rajasreeit.backend.entities.CrmAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrmAdminRepo extends JpaRepository<CrmAdmin, Integer> {

    CrmAdmin findByUsername( String username);
}
