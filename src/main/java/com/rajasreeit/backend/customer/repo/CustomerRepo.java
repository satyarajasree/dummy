package com.rajasreeit.backend.customer.repo;

import com.rajasreeit.backend.customer.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    boolean existsByAadharNumber(String aadharNumber);
    boolean existsByMobileNumber(String mobileNumber);
    boolean existsByEmail(String email);

    Customer findByMobileNumber(String mobileNumber);


}
