package com.bankapplication.repository;

import com.bankapplication.entity.Account;
import com.bankapplication.entity.Bank;
import com.bankapplication.entity.Customer;
import com.bankapplication.constant.SavingOrCurrent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomer(Customer customer);

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByCustomerAndBankAndAccountType(Customer customer, Bank bank, SavingOrCurrent accountType);

    @Query(value = "select * from account  where acc_id like :key or account_number like :key or account_type like :key or" +
            " amount like :key or ifsc_code like :key or is_blocked like :key or bank_id like :key" +
            " or cust_id like :key   ",nativeQuery = true)
    List<Account> findByTitleContent(@Param("key") String key);

   @Query(value = "select * from bankapplication.account where is_blocked like :key",nativeQuery = true)
    List<Account> findByTitleContents(@Param("key") int key);


   List<Account> findByBank(Bank bank);


}
