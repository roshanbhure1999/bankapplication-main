package com.bankapplication.repository;

import com.bankapplication.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByPanNumberOrAadharNumber(Optional<String> panNumber, Optional<String> aadharNumber);

    @Query(value = "select * from customer  where c_id like :key or aadhar_number like :key or address like :key or" +
            " city like :key or country like :key or mobile_number like :key or name like :key" +
            " or pan_number like :key   or state like :key  or zipcode like :key",nativeQuery = true)
    List<Customer> findByTitleContent(@Param("key") String key);


}
