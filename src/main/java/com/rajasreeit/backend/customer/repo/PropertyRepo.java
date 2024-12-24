package com.rajasreeit.backend.customer.repo;


import com.rajasreeit.backend.customer.entities.Properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepo extends JpaRepository<Properties, Integer> {
}
