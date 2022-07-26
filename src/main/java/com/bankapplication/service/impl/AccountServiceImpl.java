package com.bankapplication.service.impl;

import com.bankapplication.dto.AccountDto;
import com.bankapplication.entity.*;
import com.bankapplication.exception.UserException;
import com.bankapplication.repository.AccountRepository;
import com.bankapplication.repository.BankRepository;
import com.bankapplication.repository.CustomerRepository;
import com.bankapplication.repository.TransactionRepository;
import com.bankapplication.service.AccountService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final BankRepository bankRepository;

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;

    private final TransactionRepository transactionRepository;


    @Override
    public String addAccount(AccountDto accountDto) {
        Account account = dtoToEntity(accountDto);
        if (accountRepository.findByAccountNumber(account.getAccountNumber()) != null) {
            return "Account number is not unique";
        }
        if (account.getBank() == null) {
            return "Bank is not present for the given bankId ";
        }
        if (account.getCustomer() == null) {
            return "Customer is not present for the given customerId ";
        }
        List<Account> byCustomerAndBankAndAccountType = accountRepository.findByCustomerAndBankAndAccountType(account.getCustomer(), account.getBank(), account.getAccountType());

        if (byCustomerAndBankAndAccountType.size() > 0) {
            return "customer already have " + account.getAccountType().name() + " account ";
        } else if ((account.getAccountType().name() == SavingOrCurrent.SAVING.name()) && (account.getAmount() < 5000)) {
            return "Minimum Balance for saving account 5000";
        } else if ((account.getAccountType().name() == SavingOrCurrent.CURRENT.name()) && (account.getAmount() < 10000)) {
            return "Minimum Balance for Current account 10000";
        }
        account.setBlocked(false);
        accountRepository.save(account);
        return "Done New Account";
    }

    private Account dtoToEntity(AccountDto accountDto) {
        Account account = new Account();
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setAccountType(accountDto.getAccountType());
        account.setAmount(accountDto.getAmount());
        Bank bank = bankRepository.findById(accountDto.getBankId()).orElse(null);
        if (bank == null) {
            throw new UserException("The bank id is invalid for this given id " + accountDto.getBankId(), HttpStatus.BAD_REQUEST);
        }
        account.setBank(bank);
        Customer customer = customerRepository.findById(accountDto.getCustomerId()).orElse(null);
        if (customer == null) {
            throw new UserException("The customer id is invalid for this given id " + accountDto.getCustomerId(), HttpStatus.BAD_REQUEST);
        }
        account.setCustomer(customer);
        return account;
    }

    @Override
    public Account getAccount(long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            new UserException("The account id is invalid for this given id " + id, HttpStatus.BAD_REQUEST);
        }
        return account;

    }

    @Override
    @Scheduled(cron = "* 2 * * * *")
    public void transaction() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            Transaction transaction = new Transaction();
            double amount = account.getAmount();
            double totalAmount = (amount * 0.5) + amount;
            account.setAmount(totalAmount);
            accountRepository.save(account);
            transaction.setName("From bank");
            transaction.setAccountType(SavingOrCurrent.CURRENT);
            transaction.setAccountTo(account.getAccountNumber());
            transaction.setAccountFrom("Bank");
            transaction.setDate(LocalDate.now());
            transaction.setIfscCode(account.getIfscCode());

            transactionRepository.save(transaction);

        }
    }
}
