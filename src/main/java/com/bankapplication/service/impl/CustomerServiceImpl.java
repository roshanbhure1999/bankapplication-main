package com.bankapplication.service.impl;

import com.bankapplication.constant.AccountBalance;
import com.bankapplication.dto.CustomerDto;
import com.bankapplication.dto.TransactionDTO;
import com.bankapplication.entity.Account;
import com.bankapplication.entity.Customer;
import com.bankapplication.constant.SavingOrCurrent;
import com.bankapplication.entity.Transaction;
import com.bankapplication.exception.BankException;
import com.bankapplication.repository.AccountRepository;
import com.bankapplication.repository.CustomerRepository;
import com.bankapplication.repository.TransactionRepository;
import com.bankapplication.service.CustomerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    @Override
    public String addCustomer(CustomerDto customerDto) {
        String aadharNumber = customerDto.getAadharNumber();
        String panNumber = customerDto.getPanNumber();
        Customer byPanNumberOrAadharNumber = customerRepository.findByPanNumberOrAadharNumber(Optional.ofNullable(customerDto.getPanNumber()), (Optional.ofNullable(customerDto.getAadharNumber())));
        if (byPanNumberOrAadharNumber != null) {
            log.error("The Customer Already Save otherWise you can change the Aadhar number and pan number::{}",customerDto.getPanNumber()+"/+"+customerDto.getAadharNumber());
            return "PanNumberOrAadharNumber is not unique";
        }
        if (customerDto.getMobileNumber().length() != 10) {
            log.error("Invalid Mobile Number plz enter 10 digit::{}",customerDto.getMobileNumber());
            return "mobile number must be 10 digit";
        }
        if (aadharNumber.length() == 12 && panNumber.length() == 10) {
            customerRepository.save(dtoToEntity(customerDto));
            log.debug("Customer Saves Successfully");
            return "Done";
        }
        return "Invalid aadharNumber/PanNumber";
    }

    private Customer dtoToEntity(CustomerDto customerDto) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto, customer);
        return customer;
    }

    @Override
    public List<Account> getAccounts(long customerId) {

        List<Account> accounts = accountRepository.findByCustomer(customerRepository.findById(customerId).get());
        if (accounts.isEmpty()) {

            log.error("the customer is not found for this given id ::{}",customerId);
            throw new BankException("The No any account present for this given customer id " + customerId, HttpStatus.BAD_REQUEST);
        }
        log.debug("List of  account found  for this given id "+customerId );
        return accounts;
    }

    @Override
    public String getBalance(String accountNumber) {
       Optional<Account> AccountNumber = accountRepository.findByAccountNumber(accountNumber);
        Account account = AccountNumber.get();
        if (Objects.isNull(AccountNumber)){
            log.error("Account  Does  not Exist with id ::{}",accountNumber);
            return "No Account found  for this given account number "+accountNumber;
        }
        log.debug("The account balance for this given account number is "+accountNumber+" the balance is "+account.getAccountNumber()  );
        return "This Account Number " + account.getAccountNumber() + " have type " + account.getAccountType().name() + " Account  and available balance is " + account.getAmount();
    }

    @Override
    public String transferMoney(TransactionDTO transferDTO) {

        Optional<Account> fromAccountOptional = accountRepository.findByAccountNumber(transferDTO.getAccountNumberFrom());
        Optional<Account> toAccountOptional = accountRepository.findByAccountNumber(transferDTO.getAccountNumberTo());

        if (!fromAccountOptional.isPresent()) {
            log.error("No Account exist for account number:: {}", transferDTO.getAccountNumberFrom());
            throw new BankException("No Account exist for account number :: " + transferDTO.getAccountNumberFrom(), HttpStatus.NOT_FOUND);
        }
        if (!toAccountOptional.isPresent()) {
            log.error("No Account exist for account number::{}", transferDTO.getAccountNumberTo());
            throw new BankException("No Account exist for account number :: " + transferDTO.getAccountNumberTo(), HttpStatus.NOT_FOUND);
        }

        Account toAc = toAccountOptional.get();
        Account fromAc = fromAccountOptional.get();

        if (fromAc.isBlocked()) {
            log.error("From Account ::{} is locked", fromAc);
            throw new BankException(" From Account " + fromAc.getAccountNumber() + " is locked.", HttpStatus.LOCKED);
        }

        if ((fromAc.getAmount() - transferDTO.getAmount()) < 0) {
            log.error("From Account balance will go less than 0 if money is transferred.");
            throw new BankException("From Account balance will go less than 0 if money is transferred.",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        SavingOrCurrent accountType1 = fromAc.getAccountType();

        if (SavingOrCurrent.SAVING.equals(accountType1)) {
            if ((fromAc.getAmount() - transferDTO.getAmount()) < AccountBalance.CURRENT.getBalance()) {
                fromAc.setBlocked(true);
            }
        } else if (SavingOrCurrent.CURRENT.equals(accountType1)) {
            if ((fromAc.getAmount() - transferDTO.getAmount()) < AccountBalance.CURRENT.getBalance()) {
                fromAc.setBlocked(true);
            }
        }

        fromAc.setAmount(fromAc.getAmount() - transferDTO.getAmount());
        toAc.setAmount(toAc.getAmount() + transferDTO.getAmount());

        if (SavingOrCurrent.SAVING == toAc.getAccountType()) {
            if (toAc.getAmount() >= AccountBalance.SAVING.getBalance()) {
                toAc.setBlocked(false);
            }
        } else if (SavingOrCurrent.CURRENT == toAc.getAccountType()) {
            if (toAc.getAmount() >= AccountBalance.SAVING.getBalance()) {
                toAc.setBlocked(false);
            }
        }
        accountRepository.save(fromAc);
        accountRepository.save(toAc);

        Transaction transaction1 = new Transaction();

        transaction1.setAccountFrom(transferDTO.getAccountNumberFrom());
        transaction1.setAccountTo(transferDTO.getAccountNumberTo());
        transaction1.setAmount(transferDTO.getAmount());
        transaction1.setDate(LocalDate.now());
        transaction1.setIfscCode(fromAc.getIfscCode());
        transaction1.setAccountType(transferDTO.getAccountType());
        transaction1.setName(fromAc.getCustomer().getName());
        transactionRepository.save(transaction1);

        log.debug("Exiting transferAmount");
        log.debug("Transaction for amount transfer is successfully done.");
        return "SUCCESS.\nAfter transfer From Ac balance :: " + fromAc.getAmount() + "\t To Account balance :: " + toAc.getAmount();
    }



}
