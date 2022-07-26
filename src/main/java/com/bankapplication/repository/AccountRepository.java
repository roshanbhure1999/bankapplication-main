package com.bankapplication.repository;

import com.bankapplication.entity.Account;
import com.bankapplication.entity.Bank;
import com.bankapplication.entity.Customer;
import com.bankapplication.entity.SavingOrCurrent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomer(Customer customer);

    Account findByAccountNumber(String accountNumber);

    List<Account> findByCustomerAndBankAndAccountType(Customer customer, Bank bank, SavingOrCurrent accountType);


}
