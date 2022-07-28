package com.bankapplication.repository;

import com.bankapplication.entity.Account;
import com.bankapplication.entity.Bank;
import com.bankapplication.entity.Customer;
import com.bankapplication.constant.SavingOrCurrent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomer(Customer customer);

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByCustomerAndBankAndAccountType(Customer customer, Bank bank, SavingOrCurrent accountType);


}
