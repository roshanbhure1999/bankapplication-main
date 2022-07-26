package com.bankapplication.repository;

import com.bankapplication.entity.Account;
import com.bankapplication.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
 Customer findByPanNumberOrAadharNumber(Optional<String> panNumber, Optional<String> aadharNumber);


}
