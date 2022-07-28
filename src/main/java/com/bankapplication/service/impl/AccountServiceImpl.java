package com.bankapplication.service.impl;

import com.bankapplication.constant.AccountBalance;
import com.bankapplication.constant.SavingOrCurrent;
import com.bankapplication.dto.AccountDto;
import com.bankapplication.entity.*;
import com.bankapplication.exception.BankException;
import com.bankapplication.repository.AccountRepository;
import com.bankapplication.repository.BankRepository;
import com.bankapplication.repository.CustomerRepository;
import com.bankapplication.repository.TransactionRepository;
import com.bankapplication.service.AccountService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 */
@Getter
@Setter
@RequiredArgsConstructor
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final BankRepository bankRepository;

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;

    private final TransactionRepository transactionRepository;

    /**
     *
     * @param accountDto
     * @return
     *
     */
    @Override
    public String addAccount(AccountDto accountDto) {
        Account account = dtoToEntity(accountDto);
        log.trace("Entering createAccount");

        Optional<Customer> customer = customerRepository.findById(accountDto.getCustomerId());
        Optional<Bank> bank = bankRepository.findById(accountDto.getBankId());
        Bank ifsc = bankRepository.findByIfscCode(accountDto.getIfscCode());
        if (account.getAccountNumber().length()!=14){
            throw  new BankException("The Account Number Must be 14 digit",HttpStatus.BAD_REQUEST);
        }
        if (Objects.isNull(ifsc)){
            log.error("Bank already exist");
            throw new BankException("Bank already exist with IFSC", HttpStatus.CONFLICT);
        }
        if (!bank.isPresent()) {
            log.error("Bank Does  not Exist with id ::{}", accountDto.getBankId());
            throw new BankException("Bank Does  not Exist with id :: " + accountDto.getBankId(), HttpStatus.NOT_FOUND);
        }
        if (!customer.isPresent()) {
            log.error("Customer Does  not Exist with id ::{}", accountDto.getCustomerId());
            throw new BankException("Customer Does  not Exist with id :: " + accountDto.getCustomerId(), HttpStatus.NOT_FOUND);
        }
        Optional<Account> byAccountNumber = accountRepository.findByAccountNumber(account.getAccountNumber());
        if (byAccountNumber.isPresent()) {
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

        } else if (account.getAccountType().equals(SavingOrCurrent.SAVING) && accountDto.getAmount() < AccountBalance.SAVING.getBalance()) {
            log.error("Minimum balance to open saving account is {}", AccountBalance.SAVING.getBalance());
            throw new BankException(String.format("Minimum balance to open saving account is %s", AccountBalance.SAVING.getBalance()), HttpStatus.BAD_REQUEST);

        } else if (account.getAccountType().equals(SavingOrCurrent.CURRENT) && accountDto.getAmount() < AccountBalance.CURRENT.getBalance()) {
            log.error("Minimum balance to open current account is {}", AccountBalance.CURRENT.getBalance());
            throw new BankException(String.format("Minimum balance to open current account is %s", AccountBalance.CURRENT.getBalance()), HttpStatus.BAD_REQUEST);
        }
        account.setBlocked(false);
        accountRepository.save(account);
        log.debug("Account Saves Successfully");
        return "Done New Account";
    }

    /**
     *
     * @param accountDto
     * @return Account
     *
     */
    private Account dtoToEntity(AccountDto accountDto) {
        Account account = new Account();
        BeanUtils.copyProperties(accountDto,account);
        Bank bank = bankRepository.findById(accountDto.getBankId()).orElse(null);
        if (Objects.isNull(bank)){
            throw new BankException("The bank not found for this given bank id :: "+accountDto.getBankId(),HttpStatus.BAD_REQUEST);
        }
        account.setBank(bank);
        Optional<Customer> customer = customerRepository.findById(accountDto.getCustomerId());
        Customer customer1 = customer.get();
        if (Objects.isNull(customer1)){
            throw new BankException("The customer not found for this given bank id :: "+accountDto.getCustomerId(),HttpStatus.BAD_REQUEST);
        }
        account.setCustomer(customer1);
        log.debug("Account Saves Successfully");
        return account;
    }


    private Customer accountValidation(AccountDto accountDto, Account account, Bank bank) {
        if (bank == null) {
            log.error("Bank Does  not Exist with id ::{}", accountDto.getBankId());
            throw new BankException("The bank id is invalid for this given id " + accountDto.getBankId(), HttpStatus.BAD_REQUEST);
        }
        account.setBank(bank);
        Customer customer = customerRepository.findById(accountDto.getCustomerId()).orElse(null);
        if (customer == null) {
            log.error("Customer Does  not Exist with id ::{}", accountDto.getCustomerId());
            throw new BankException("The customer id is invalid for this given id " + accountDto.getCustomerId(), HttpStatus.BAD_REQUEST);
        }
        return customer;
    }

    /**
     *
     * @param id
     * @return Account
     */
    @Override
    public Account getAccount(long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            log.error("Bank Does  not Exist with id ::{}",id);
            new BankException("The account id is invalid for this given id " + id, HttpStatus.BAD_REQUEST);
        }
        log.debug("Account get  By id  Successfully"+account);
        return account;

    }

    /**
     * @apiNote  this method automatically update the account amount
     *              after two min and transaction add in transaction table
     *     @Scheduled(cron = "0 0/10 * * * *") // Once in 10 mins
     *     //@Scheduled(cron = "0 0 0 * * *") //once in day
     *
     */
    @Override

    @Scheduled(cron = "0 0/10 * * * *") // Once in 10 mins
    //@Scheduled(cron = "0 0 0 * * *") //once in day

    public void transaction() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            log.debug("Processing interest for account :: " + account.getAccountId());
            Transaction transaction = new Transaction();
            double amount = account.getAmount();
            double totalAmount = (amount * AccountBalance.Interest.getBalance()) + amount;
            Double interest = ((amount *AccountBalance.Interest.getBalance()) / AccountBalance.Total.getBalance());
            account.setAmount(totalAmount);
            accountRepository.save(account);
            transaction.setName("From bank");
            transaction.setAccountType(SavingOrCurrent.CURRENT);
            transaction.setAccountTo(account.getAccountNumber());
            transaction.setAccountFrom("Bank");
            transaction.setDate(LocalDate.now());
            transaction.setIfscCode(account.getIfscCode());
            log.debug("existingBalance :: " + amount + "\tinterest :: " + interest + "\tnewValue :: " + totalAmount);
            transactionRepository.save(transaction);

        }
    }
}
