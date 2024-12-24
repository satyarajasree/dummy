package com.rajasreeit.backend.service.userdetails;

import com.rajasreeit.backend.customer.entities.Customer;
import com.rajasreeit.backend.entities.principal.CustomerPrincipal;
import com.rajasreeit.backend.customer.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepo customerRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepo.findByMobileNumber(username);
        if(customer == null){
            new UsernameNotFoundException("customer not found");
        }
        return new CustomerPrincipal(customer);
    }
}
