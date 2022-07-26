package com.bankapplication.Controller;

import com.bankapplication.dto.CustomerDto;
import com.bankapplication.dto.TransactionDTO;
import com.bankapplication.entity.Account;
import com.bankapplication.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> addCustomer(@Valid  @RequestBody CustomerDto customerDto) {
        String s = customerService.addCustomer(customerDto);
        return new ResponseEntity<String>(s, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{custId}")
    public ResponseEntity<List<Account>> getAccounts(@PathVariable("custId") long id) {
        List<Account> accounts = customerService.getAccounts(id);
        return new ResponseEntity<List<Account>>(accounts, HttpStatus.CREATED);
    }

    @GetMapping(path = "/get-balance/{account-number}")
    public ResponseEntity<String> getBalance(@PathVariable("account-number") String accountNumber) {
        String balance = customerService.getBalance(accountNumber);
        return new ResponseEntity<>(balance,HttpStatus.CREATED);
    }

    @PutMapping(path = "/transfer")
    public ResponseEntity<String>  transferMoney(@RequestBody TransactionDTO transferDTO){
        String s = customerService.transferMoney(transferDTO);
        return new ResponseEntity<>(s,HttpStatus.CREATED);
    }

}
