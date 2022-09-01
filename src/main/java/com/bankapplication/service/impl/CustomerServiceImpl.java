package com.bankapplication.service.impl;

import com.bankapplication.constant.AccountBalance;
import com.bankapplication.dto.CustomerDto;
import com.bankapplication.dto.TransactionDTO;
import com.bankapplication.entity.Account;
import com.bankapplication.entity.Customer;
import com.bankapplication.constant.SavingOrCurrent;
import com.bankapplication.entity.Transaction;

import com.bankapplication.repository.AccountRepository;
import com.bankapplication.repository.CustomerRepository;
import com.bankapplication.repository.TransactionRepository;
import com.bankapplication.service.CustomerService;
import com.commonexception.exception.BankException;
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
import java.util.stream.Collectors;

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
        Customer byPanNumberOrAadharNumber = customerRepository.findByPanNumberOrAadharNumber(customerDto.getPanNumber(),customerDto.getAadharNumber());
        if (byPanNumberOrAadharNumber != null) {
            log.error("The Customer Already Save otherWise you can change the Aadhar number and pan number::{}",customerDto.getPanNumber()+"/+"+customerDto.getAadharNumber());
            throw new BankException("Not allowed to update Aadhar number And Pan Number", HttpStatus.BAD_REQUEST);
        }
        if (customerDto.getMobileNumber().length() != 10) {
            log.error("Invalid Mobile Number plz enter 10 digit::{}",customerDto.getMobileNumber());
            throw new BankException("The mobile number must be 10 digit", HttpStatus.BAD_REQUEST);
        }
        if (aadharNumber.length() == 12 && panNumber.length() == 10) {
            customerRepository.save(dtoToEntity(customerDto));
            log.debug("Customer Saves Successfully");
            return "Done";
        }
        throw new BankException("Customer should have unique Adhar/Pan Number.", HttpStatus.BAD_REQUEST);
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
            throw new BankException("Account  Does  not Exist with id ::{}"+accountNumber, HttpStatus.NO_CONTENT);
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

    @Override
    public CustomerDto findById(Long id) {
        Optional<Customer> byId = customerRepository.findById(id);
        Customer result= byId.orElseThrow(()-> new BankException("Not found", HttpStatus.NO_CONTENT));

            CustomerDto customerDto=new CustomerDto();
            BeanUtils.copyProperties(result,customerDto);
            customerDto.setId(result.getCId());
            return customerDto;


    }

    @Override
    public List<CustomerDto> findAll() {
        List<CustomerDto> customer = customerRepository.findAll().stream().map(this::toCustomerDTO).collect(Collectors.toList());

        if (!customer.isEmpty()) {
            return customer;
        } else throw new BankException("", HttpStatus.NO_CONTENT);
    }

    @Override
    public String deleteById(long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new BankException("first you need to close the account ", HttpStatus.BAD_REQUEST));
        List<Account> listOfAccount = accountRepository.findByCustomer(customer);
        if (!listOfAccount.isEmpty()){
            throw new BankException("first you need to close the account ",HttpStatus.BAD_REQUEST);
        }
        customerRepository.deleteById(id);
        return String.format("The customer delete successfully for this given id %d", id);
    }

    @Override
    public List<CustomerDto> search(String key) {
        List<CustomerDto> customer = customerRepository.findByTitleContent("%" + key + "%").stream().map(this::toCustomerDTO).collect(Collectors.toList());
        if (!customer.isEmpty()) {
            return customer;
        } else throw new BankException("", HttpStatus.NO_CONTENT);
    }

    @Override
    public String updateCustomer(CustomerDto customerDto) {
        Optional<Customer> customer = customerRepository.findById(customerDto.getId());
        if (!customer.isPresent()) {
            log.error("No Customer exist for account number:: {}", customerDto.getId());
            throw new BankException("No Customer exist for Customer number :: " + customerDto.getId(), HttpStatus.NOT_FOUND);
        }
        if (customerDto.getAadharNumber().length() != 12 && customerDto.getPanNumber().length() != 10&&customerDto.getMobileNumber().length() != 10) {

            throw new BankException("Customer should have  Adhar(12)/Pan Number(10)/mobile number(10)", HttpStatus.NOT_FOUND);

        }

        Customer customer2 = customer.get();
        customer2.setName(customerDto.getName());
        customer2.setAddress(customerDto.getAddress());
        customer2.setCity(customerDto.getCity());
        customer2.setCountry(customerDto.getCountry());
        customer2.setMobileNumber(customerDto.getMobileNumber());
        customer2.setZipcode(customerDto.getZipcode());
        customer2.setState(customerDto.getState());
        customerRepository.save(customer2);
        return "the customer update  successfully";
    }

    private CustomerDto toCustomerDTO(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        customerDto.setId(customer.getCId());
        return customerDto;
    }

}
