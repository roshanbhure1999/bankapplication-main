package com.bankapplication.repository;


import com.bankapplication.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

          Bank findByIfscCode(String ifscCode);

}
