package com.bankapplication.service;

import com.bankapplication.dto.CustomerDto;
import com.bankapplication.dto.TransactionDTO;
import com.bankapplication.entity.Account;

import java.util.List;

public interface CustomerService {
    String addCustomer(CustomerDto customerDto);

    List<Account> getAccounts(long customerId);

    String getBalance(String accountNumber);

    String transferMoney(TransactionDTO transferDTO);
}
