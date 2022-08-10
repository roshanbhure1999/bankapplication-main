package com.bankapplication.service;

import com.bankapplication.dto.CustomerDto;
import com.bankapplication.dto.TransactionDTO;
import com.bankapplication.entity.Account;
import com.bankapplication.entity.Customer;

import java.util.List;

public interface CustomerService {
    String addCustomer(CustomerDto customerDto);

    List<Account> getAccounts(long customerId);

    String getBalance(String accountNumber);

    String transferMoney(TransactionDTO transferDTO);

    CustomerDto findById(Long id);

    List<CustomerDto> findAll();

    String deleteById(long id);

    List<CustomerDto> search(String key);

    String updateCustomer(CustomerDto customerDto);
}
