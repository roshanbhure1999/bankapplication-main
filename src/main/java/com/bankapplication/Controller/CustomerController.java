package com.bankapplication.Controller;

import com.bankapplication.constantUrl.Url;
import com.bankapplication.dto.CustomerDto;
import com.bankapplication.dto.TransactionDTO;
import com.bankapplication.entity.Account;
import com.bankapplication.service.CustomerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@RestController
@RequestMapping(path = Url.CUSTOMER)
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> addCustomer(@Valid @RequestBody CustomerDto customerDto) {
        String s = customerService.addCustomer(customerDto);
        return new ResponseEntity<String>(s, HttpStatus.CREATED);
    }
    @GetMapping(Url.ID)
    public ResponseEntity<CustomerDto> findCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }
    @GetMapping(path = Url.CUSTOMER+Url.ID)
    public ResponseEntity<List<Account>> getAccounts(@PathVariable("id") long id) {
        List<Account> accounts = customerService.getAccounts(id);
        return new ResponseEntity<List<Account>>(accounts, HttpStatus.CREATED);
    }

    @GetMapping(path = Url.GET_BALANCE)
    public ResponseEntity<String> getBalance(@PathVariable("account-number") String accountNumber) {
        String balance = customerService.getBalance(accountNumber);
        return new ResponseEntity<>(balance, HttpStatus.CREATED);
    }

    @PutMapping(path = "/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransactionDTO transferDTO) {
        String s = customerService.transferMoney(transferDTO);
        return new ResponseEntity<>(s, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @DeleteMapping(Url.ID)
    public ResponseEntity<String> deleteById(@PathVariable long id){
        return ResponseEntity.ok(customerService.deleteById(id));
    }

    @GetMapping(Url.LIKE)
    public ResponseEntity<List<CustomerDto>> search(@PathVariable("content") String content){
        return ResponseEntity.ok(customerService.search(content));

    }

    @PutMapping(Url.UPDATE)
    public ResponseEntity<String> updateCustomer(@Valid @RequestBody CustomerDto customerDto) {
        String s = customerService.updateCustomer(customerDto);
        return new ResponseEntity<String>(s, HttpStatus.OK);
    }

}
