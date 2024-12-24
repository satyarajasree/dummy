package com.rajasreeit.backend.service;

import com.rajasreeit.backend.entities.CrmAdmin;
import com.rajasreeit.backend.entities.CrmAdminPrincipal;
import com.rajasreeit.backend.repo.CrmAdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Primary
@Service("crmDetailsService")
public class CrmDetailsService implements UserDetailsService {

    @Autowired
    private CrmAdminRepo crmAdminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CrmAdmin crmAdmin = crmAdminRepo.findByUsername(username);
        if(crmAdmin == null){
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("user not found");
        }
        return new CrmAdminPrincipal(crmAdmin);
    }
}
