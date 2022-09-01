package com.bankapplication.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bankapplication.constant.SavingOrCurrent;
import com.bankapplication.dto.AccountDto;
import com.bankapplication.entity.Account;
import com.bankapplication.entity.Bank;
import com.bankapplication.entity.Customer;
import com.bankapplication.repository.AccountRepository;
import com.bankapplication.repository.BankRepository;
import com.bankapplication.repository.CustomerRepository;
import com.bankapplication.repository.TransactionRepository;
import com.commonexception.exception.BankException;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AccountServiceImpl.class})
@ExtendWith(SpringExtension.class)
class AccountServiceImplTest {
    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @MockBean
    private BankRepository bankRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    /**
     * Method under test: {@link AccountServiceImpl#updateAccount(AccountDto)}
     */
    @Test
    void testUpdateAccount() {
        Bank bank = new Bank();
        bank.setAddress("42 Main St");
        bank.setBId(123L);
        bank.setBankName("Bank Name");
        bank.setBranchName("janedoe/featurebranch");
        bank.setCity("Oxford");
        bank.setCountry("GB");
        bank.setIfscCode("Ifsc Code");
        bank.setState("MD");
        bank.setZipcode("21654");
        Optional<Bank> ofResult = Optional.of(bank);
        when(bankRepository.findById((Long) any())).thenReturn(ofResult);

        Bank bank1 = new Bank();
        bank1.setAddress("42 Main St");
        bank1.setBId(123L);
        bank1.setBankName("Bank Name");
        bank1.setBranchName("janedoe/featurebranch");
        bank1.setCity("Oxford");
        bank1.setCountry("GB");
        bank1.setIfscCode("Ifsc Code");
        bank1.setState("MD");
        bank1.setZipcode("21654");

        Customer customer = new Customer();
        customer.setAadharNumber("42");
        customer.setAddress("42 Main St");
        customer.setCId(123L);
        customer.setCity("Oxford");
        customer.setCountry("GB");
        customer.setMobileNumber("42");
        customer.setName("Name");
        customer.setPanNumber("42");
        customer.setState("MD");
        customer.setZipcode("21654");

        Account account = new Account();
        account.setAccountId(1234567890L);
        account.setAccountNumber("42");
        account.setAccountType(SavingOrCurrent.SAVING);
        account.setAmount(10.0d);
        account.setBank(bank1);
        account.setBlocked(true);
        account.setCustomer(customer);
        account.setIfscCode("Ifsc Code");
        Optional<Account> ofResult1 = Optional.of(account);

        Bank bank2 = new Bank();
        bank2.setAddress("42 Main St");
        bank2.setBId(123L);
        bank2.setBankName("Bank Name");
        bank2.setBranchName("janedoe/featurebranch");
        bank2.setCity("Oxford");
        bank2.setCountry("GB");
        bank2.setIfscCode("Ifsc Code");
        bank2.setState("MD");
        bank2.setZipcode("21654");

        Customer customer1 = new Customer();
        customer1.setAadharNumber("42");
        customer1.setAddress("42 Main St");
        customer1.setCId(123L);
        customer1.setCity("Oxford");
        customer1.setCountry("GB");
        customer1.setMobileNumber("42");
        customer1.setName("Name");
        customer1.setPanNumber("42");
        customer1.setState("MD");
        customer1.setZipcode("21654");

        Account account1 = new Account();
        account1.setAccountId(1234567890L);
        account1.setAccountNumber("42");
        account1.setAccountType(SavingOrCurrent.SAVING);
        account1.setAmount(10.0d);
        account1.setBank(bank2);
        account1.setBlocked(true);
        account1.setCustomer(customer1);
        account1.setIfscCode("Ifsc Code");
        when(accountRepository.save((Account) any())).thenReturn(account1);
        when(accountRepository.findById((Long) any())).thenReturn(ofResult1);

        Customer customer2 = new Customer();
        customer2.setAadharNumber("42");
        customer2.setAddress("42 Main St");
        customer2.setCId(123L);
        customer2.setCity("Oxford");
        customer2.setCountry("GB");
        customer2.setMobileNumber("42");
        customer2.setName("Name");
        customer2.setPanNumber("42");
        customer2.setState("MD");
        customer2.setZipcode("21654");
        Optional<Customer> ofResult2 = Optional.of(customer2);
        when(customerRepository.findById((Long) any())).thenReturn(ofResult2);

        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("42");
        accountDto.setAccountType(SavingOrCurrent.SAVING);
        accountDto.setAmount(10.0d);
        accountDto.setBankId(123L);
        accountDto.setBlocked(true);
        accountDto.setCustomerId(123L);
        accountDto.setId(123L);
        accountDto.setIfscCode("Ifsc Code");
        assertEquals("the account delete successfully", accountServiceImpl.updateAccount(accountDto));
        verify(bankRepository).findById((Long) any());
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findById((Long) any());
        verify(customerRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateAccount(AccountDto)}
     */
    @Test
    void testUpdateAccount2() {
        Bank bank = new Bank();
        bank.setAddress("42 Main St");
        bank.setBId(123L);
        bank.setBankName("Bank Name");
        bank.setBranchName("janedoe/featurebranch");
        bank.setCity("Oxford");
        bank.setCountry("GB");
        bank.setIfscCode("Ifsc Code");
        bank.setState("MD");
        bank.setZipcode("21654");
        Optional<Bank> ofResult = Optional.of(bank);
        when(bankRepository.findById((Long) any())).thenReturn(ofResult);

        Bank bank1 = new Bank();
        bank1.setAddress("42 Main St");
        bank1.setBId(123L);
        bank1.setBankName("Bank Name");
        bank1.setBranchName("janedoe/featurebranch");
        bank1.setCity("Oxford");
        bank1.setCountry("GB");
        bank1.setIfscCode("Ifsc Code");
        bank1.setState("MD");
        bank1.setZipcode("21654");

        Customer customer = new Customer();
        customer.setAadharNumber("42");
        customer.setAddress("42 Main St");
        customer.setCId(123L);
        customer.setCity("Oxford");
        customer.setCountry("GB");
        customer.setMobileNumber("42");
        customer.setName("Name");
        customer.setPanNumber("42");
        customer.setState("MD");
        customer.setZipcode("21654");

        Account account = new Account();
        account.setAccountId(1234567890L);
        account.setAccountNumber("42");
        account.setAccountType(SavingOrCurrent.SAVING);
        account.setAmount(10.0d);
        account.setBank(bank1);
        account.setBlocked(true);
        account.setCustomer(customer);
        account.setIfscCode("Ifsc Code");
        Optional<Account> ofResult1 = Optional.of(account);
        when(accountRepository.save((Account) any()))
                .thenThrow(new BankException("An error occurred", HttpStatus.CONTINUE));
        when(accountRepository.findById((Long) any())).thenReturn(ofResult1);

        Customer customer1 = new Customer();
        customer1.setAadharNumber("42");
        customer1.setAddress("42 Main St");
        customer1.setCId(123L);
        customer1.setCity("Oxford");
        customer1.setCountry("GB");
        customer1.setMobileNumber("42");
        customer1.setName("Name");
        customer1.setPanNumber("42");
        customer1.setState("MD");
        customer1.setZipcode("21654");
        Optional<Customer> ofResult2 = Optional.of(customer1);
        when(customerRepository.findById((Long) any())).thenReturn(ofResult2);

        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("42");
        accountDto.setAccountType(SavingOrCurrent.SAVING);
        accountDto.setAmount(10.0d);
        accountDto.setBankId(123L);
        accountDto.setBlocked(true);
        accountDto.setCustomerId(123L);
        accountDto.setId(123L);
        accountDto.setIfscCode("Ifsc Code");
        assertThrows(BankException.class, () -> accountServiceImpl.updateAccount(accountDto));
        verify(bankRepository).findById((Long) any());
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findById((Long) any());
        verify(customerRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateAccount(AccountDto)}
     */
    @Test
    void testUpdateAccount3() {
        when(bankRepository.findById((Long) any())).thenReturn(Optional.empty());

        Bank bank = new Bank();
        bank.setAddress("42 Main St");
        bank.setBId(123L);
        bank.setBankName("Bank Name");
        bank.setBranchName("janedoe/featurebranch");
        bank.setCity("Oxford");
        bank.setCountry("GB");
        bank.setIfscCode("Ifsc Code");
        bank.setState("MD");
        bank.setZipcode("21654");

        Customer customer = new Customer();
        customer.setAadharNumber("42");
        customer.setAddress("42 Main St");
        customer.setCId(123L);
        customer.setCity("Oxford");
        customer.setCountry("GB");
        customer.setMobileNumber("42");
        customer.setName("Name");
        customer.setPanNumber("42");
        customer.setState("MD");
        customer.setZipcode("21654");

        Account account = new Account();
        account.setAccountId(1234567890L);
        account.setAccountNumber("42");
        account.setAccountType(SavingOrCurrent.SAVING);
        account.setAmount(10.0d);
        account.setBank(bank);
        account.setBlocked(true);
        account.setCustomer(customer);
        account.setIfscCode("Ifsc Code");
        Optional<Account> ofResult = Optional.of(account);

        Bank bank1 = new Bank();
        bank1.setAddress("42 Main St");
        bank1.setBId(123L);
        bank1.setBankName("Bank Name");
        bank1.setBranchName("janedoe/featurebranch");
        bank1.setCity("Oxford");
        bank1.setCountry("GB");
        bank1.setIfscCode("Ifsc Code");
        bank1.setState("MD");
        bank1.setZipcode("21654");

        Customer customer1 = new Customer();
        customer1.setAadharNumber("42");
        customer1.setAddress("42 Main St");
        customer1.setCId(123L);
        customer1.setCity("Oxford");
        customer1.setCountry("GB");
        customer1.setMobileNumber("42");
        customer1.setName("Name");
        customer1.setPanNumber("42");
        customer1.setState("MD");
        customer1.setZipcode("21654");

        Account account1 = new Account();
        account1.setAccountId(1234567890L);
        account1.setAccountNumber("42");
        account1.setAccountType(SavingOrCurrent.SAVING);
        account1.setAmount(10.0d);
        account1.setBank(bank1);
        account1.setBlocked(true);
        account1.setCustomer(customer1);
        account1.setIfscCode("Ifsc Code");
        when(accountRepository.save((Account) any())).thenReturn(account1);
        when(accountRepository.findById((Long) any())).thenReturn(ofResult);

        Customer customer2 = new Customer();
        customer2.setAadharNumber("42");
        customer2.setAddress("42 Main St");
        customer2.setCId(123L);
        customer2.setCity("Oxford");
        customer2.setCountry("GB");
        customer2.setMobileNumber("42");
        customer2.setName("Name");
        customer2.setPanNumber("42");
        customer2.setState("MD");
        customer2.setZipcode("21654");
        Optional<Customer> ofResult1 = Optional.of(customer2);
        when(customerRepository.findById((Long) any())).thenReturn(ofResult1);

        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("42");
        accountDto.setAccountType(SavingOrCurrent.SAVING);
        accountDto.setAmount(10.0d);
        accountDto.setBankId(123L);
        accountDto.setBlocked(true);
        accountDto.setCustomerId(123L);
        accountDto.setId(123L);
        accountDto.setIfscCode("Ifsc Code");
        assertThrows(BankException.class, () -> accountServiceImpl.updateAccount(accountDto));
        verify(bankRepository).findById((Long) any());
        verify(accountRepository).findById((Long) any());
    }
}

