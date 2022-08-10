package com.bankapplication.service.impl;

import com.bankapplication.constant.AccountBalance;
import com.bankapplication.constant.SavingOrCurrent;
import com.bankapplication.dto.AccountDto;
import com.bankapplication.entity.Account;
import com.bankapplication.entity.Bank;
import com.bankapplication.entity.Customer;
import com.bankapplication.entity.Transaction;
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
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * @param accountDto
     * @return
     */
    @Override
    public String addAccount(AccountDto accountDto) {


        String random;
        Optional<Account> byAccountNumber1;
        do {
            random = RandomStringUtils.random(14, false, true);
            byAccountNumber1 = accountRepository.findByAccountNumber(random);
        } while (Objects.isNull(byAccountNumber1));
        accountDto.setAccountNumber(random);
        Account account = dtoToEntity(accountDto);
        log.trace("Entering createAccount");
        Optional<Customer> customer = customerRepository.findById(accountDto.getCustomerId());
        Optional<Bank> bank = bankRepository.findById(accountDto.getBankId());
        Bank ifsc = bankRepository.findByIfscCode(accountDto.getIfscCode());
        if (account.getAccountNumber().length() != 14) {
            throw new BankException("The Account Number Must be 14 digit", HttpStatus.BAD_REQUEST);
        }
        if (Objects.isNull(ifsc)) {
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
        Account byAccountNumber = accountRepository.findByAccountNumber(account.getAccountNumber()).orElseThrow(() -> new BankException("Account number is not unique", HttpStatus.BAD_REQUEST));
        if (account.getBank() == null) {
            throw new BankException("Bank is not present for the given bankId", HttpStatus.BAD_REQUEST);
        }
        if (account.getCustomer() == null) {
            throw new BankException("Customer is not present for the given customerId", HttpStatus.BAD_REQUEST);
        }
        List<Account> byCustomerAndBankAndAccountType = accountRepository.findByCustomerAndBankAndAccountType(account.getCustomer(), account.getBank(), account.getAccountType());

        if (byCustomerAndBankAndAccountType.size() > 0) {
            throw new BankException("customer already have " + account.getAccountType().name() + " account ", HttpStatus.BAD_REQUEST);


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
     * @param accountDto
     * @return Account
     */
    private Account dtoToEntity(AccountDto accountDto) {
        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);
        Bank bank = bankRepository.findById(accountDto.getBankId()).orElseThrow(() -> new BankException("The bank not found for this given bank id :: " + accountDto.getBankId(), HttpStatus.BAD_REQUEST));
        account.setBank(bank);
        Customer customer = customerRepository.findById(accountDto.getCustomerId()).orElseThrow(() -> new BankException("The customer not found for this given bank id :: " + accountDto.getCustomerId(), HttpStatus.BAD_REQUEST));
        account.setCustomer(customer);
        log.debug("Account Saves Successfully");
        return account;
    }


    /**
     * @param id
     * @return Account
     */
    @Override
    public Account getAccount(long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new BankException("The account id is invalid for this given id " + id, HttpStatus.BAD_REQUEST));
        log.debug("Account get  By id  Successfully" + account);
        return account;

    }

    /**
     * @apiNote this method automatically update the account amount
     * after two min and transaction add in transaction table
     * @Scheduled(cron = "0 0/10 * * * *") // Once in 10 mins
     * //@Scheduled(cron = "0 0 0 * * *") //once in day
     */
    @Override

    //  @Scheduled(cron = "0 0/2 * * * *") // Once in 10 mins
    //@Scheduled(cron = "0 0 0 * * *") //once in day

    public void transaction() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            log.debug("Processing interest for account :: " + account.getAccountId());
            Transaction transaction = new Transaction();
            double amount = account.getAmount();
            double interest = amount * AccountBalance.Interest.getBalance() / AccountBalance.Total.getBalance();
            double totalAmount = (interest) + amount;
            account.setAmount(totalAmount);
            accountRepository.save(account);
            transaction.setName("From bank");
            transaction.setAccountType(SavingOrCurrent.CURRENT);
            transaction.setAccountTo(account.getAccountNumber());
            transaction.setAccountFrom("Bank");
            transaction.setAmount(totalAmount);
            transaction.setDate(LocalDate.now());
            transaction.setIfscCode(account.getIfscCode());
            log.debug("existingBalance :: " + amount + "\tinterest :: " + interest + "\tnewValue :: " + totalAmount);
            transactionRepository.save(transaction);

        }
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<AccountDto> banks = accountRepository.findAll().stream().map(this::toAccountDTO).collect(Collectors.toList());
        if (!banks.isEmpty()) {
            return banks;
        } else throw new BankException("Not found", HttpStatus.NO_CONTENT);
    }

    @Override
    public AccountDto findByAccountNumber(String accountNumber) {

        AccountDto account = accountRepository.findByAccountNumber(accountNumber).map(this::toAccountDTO).orElseThrow(() -> new BankException("", HttpStatus.NO_CONTENT));
        return account;

    }

    @Override
    public List<AccountDto> search(String content) {
        List<AccountDto> account = accountRepository.findByTitleContent("%" + content + "%").stream().map(this::toAccountDTO).collect(Collectors.toList());
        if (!account.isEmpty()) {
            return account;
        } else throw new BankException("", HttpStatus.NO_CONTENT);
    }

    @Override
    public List<AccountDto> searchBlockAccount(int content) {
        List<AccountDto> account = accountRepository.findByTitleContents(content).stream().map(this::toAccountDTO).collect(Collectors.toList());
        if (!account.isEmpty()) {
            return account;
        } else throw new BankException("", HttpStatus.NO_CONTENT);

    }

    @Override
    public String deleteById(long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new BankException("the Account not present for this given id " + id, HttpStatus.BAD_REQUEST));
        accountRepository.deleteById(id);
        return "the account delete successfully";
    }

    @Override
    public String updateAccount(AccountDto accountDto) {
        Account account = this.accountRepository.findById(accountDto.getBankId()).orElseThrow(() -> new BankException("The account not present for this given id ", HttpStatus.BAD_REQUEST));
        Account account1 = dtoToEntity(accountDto);
        accountRepository.save(account1);
        return "the account delete successfully";

    }


    private AccountDto toAccountDTO(Account account) {
        AccountDto accountDto = new AccountDto();
        BeanUtils.copyProperties(account, accountDto);
        accountDto.setId(account.getAccountId());
        accountDto.setCustomerId(account.getCustomer().getCId());
        accountDto.setBankId(account.getBank().getBId());
        return accountDto;
    }


}
