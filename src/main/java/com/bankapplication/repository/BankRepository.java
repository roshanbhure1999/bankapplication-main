package com.bankapplication.repository;


import com.bankapplication.entity.Bank;
import com.bankapplication.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

            Bank findByIfscCode(String ifscCode);

    @Query(value = "select * from bank  where b_id like :key or address like :key or bank_name like :key or" +
            " branch_name like :key or country like :key or city like :key or ifsc_code like :key" +
            " or state like :key or zipcode like :key",nativeQuery = true)
    List<Bank> findByTitleContent(@Param("key") String key);

}
