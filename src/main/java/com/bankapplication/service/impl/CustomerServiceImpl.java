package com.bankapplication.service.impl;

import com.bankapplication.dto.CustomerDto;
import com.bankapplication.dto.TransactionDTO;
import com.bankapplication.entity.Account;
import com.bankapplication.entity.Customer;
import com.bankapplication.entity.SavingOrCurrent;
import com.bankapplication.entity.Transaction;
import com.bankapplication.exception.UserException;
import com.bankapplication.repository.AccountRepository;
import com.bankapplication.repository.CustomerRepository;
import com.bankapplication.repository.TransactionRepository;
import com.bankapplication.service.CustomerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Getter
@Setter
@RequiredArgsConstructor
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
            return "PanNumberOrAadharNumber is not unique";
        }
        if (customerDto.getMobileNumber().length() != 10) {
            return "mobile number must be 10 digit";
        }
        if (aadharNumber.length() == 12 && panNumber.length() == 10) {
            customerRepository.save(dtoToEntity(customerDto));
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
            throw new UserException("The No any account present for this given customer id " + customerId, HttpStatus.BAD_REQUEST);
        }
        return accounts;
    }

    @Override
    public String getBalance(String accountNumber) {
        Account byAccountNumber = accountRepository.findByAccountNumber(accountNumber);
        return "This Account Number " + byAccountNumber.getAccountNumber() + " have type " + byAccountNumber.getAccountType().name() + " Account  and available balance is " + byAccountNumber.getAmount();
    }

    @Override
    public String transferMoney(TransactionDTO transferDTO) {

        Account fromAccount = accountRepository.findByAccountNumber(transferDTO.getAccountNumberFrom());
        Account toAccount = accountRepository.findByAccountNumber(transferDTO.getAccountNumberTo());

        if (fromAccount == null) {
            throw new UserException("No Account found for this given Account Number " + transferDTO.getAccountNumberFrom(), HttpStatus.BAD_REQUEST);
        }
        if (toAccount == null) {
            throw new UserException("No Account found for this given Account Number " + transferDTO.getAccountNumberTo(), HttpStatus.BAD_REQUEST);
        }

        if (!fromAccount.isBlocked()) {
            if (!transferDTO.getIfscCode().equals(toAccount.getIfscCode())) {
                return "IFSC does not match";
            }
            if (transferDTO.getAmount() > fromAccount.getAmount()) {
                return "Insufficient Balance";
            }
            double amountTo = fromAccount.getAmount() - transferDTO.getAmount();
            double toAmount = toAccount.getAmount() + transferDTO.getAmount();

            fromAccount.setAmount(amountTo);
            toAccount.setAmount(toAmount);
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            Transaction transaction = dtoToEntity(transferDTO);
            transactionRepository.save(transaction);

            Account fromAccount1 = accountRepository.findByAccountNumber(transferDTO.getAccountNumberFrom());

            if ((fromAccount1.getAccountType().name()== SavingOrCurrent.SAVING.name() && (amountTo < 5000))) {
                fromAccount1.setBlocked(true);
                accountRepository.save(fromAccount1);
                transactionRepository.save(transaction);
                return "transaction successful but account is blocked";
            }

            if ((fromAccount1.getAccountType().name()== SavingOrCurrent.CURRENT.name() && (amountTo < 10000))) {
                fromAccount1.setBlocked(true);
                accountRepository.save(fromAccount1);
                transactionRepository.save(transaction);
                return "transaction successful but account is blocked";
            }
            Account toAccount1 = accountRepository.findByAccountNumber(transferDTO.getAccountNumberTo());
            if ((toAccount1.getAccountType().name()==SavingOrCurrent.SAVING.name() && (amountTo < 5000))) {
                toAccount1.setBlocked(false);
                accountRepository.save(toAccount1);
                transactionRepository.save(transaction);
                return "transaction successful but account is blocked";
            }

            if ((toAccount1.getAccountType().name()==SavingOrCurrent.CURRENT.name() && (amountTo < 10000))) {
                toAccount1.setBlocked(false);
                accountRepository.save(toAccount1);
                transactionRepository.save(transaction);
                return "transaction successful but account is blocked";
            }
            return "Successful transaction ";
        }else  return "account is blocked";
    }

    private Transaction dtoToEntity(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionDTO, transaction);
        return transaction;
    }


}
