package com.rajasreeit.backend.service.userdetails;

import com.rajasreeit.backend.entities.CrmEmployee;
import com.rajasreeit.backend.entities.principal.CrmEmployeePrincipal;
import com.rajasreeit.backend.repo.CrmEmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("crmEmployeeDetailService")
public class CrmEmployeeDetailService implements UserDetailsService {

    @Autowired
    private CrmEmployeeRepo crmEmployeeRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CrmEmployee crmEmployee = crmEmployeeRepo.findByMobile(username);
        if(crmEmployee == null){
            new UsernameNotFoundException("Employee not found");
        }
        return new CrmEmployeePrincipal(crmEmployee);
    }
}
