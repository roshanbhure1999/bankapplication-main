package com.bankapplication.Controller;

import com.bankapplication.dto.AccountDto;
import com.bankapplication.entity.Account;
import com.bankapplication.service.AccountService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Getter
@Setter
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<String> createAccount(@Valid @RequestBody AccountDto accountDto) {
        String s = accountService.addAccount(accountDto);
        return new ResponseEntity<String>(s, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{accId}")
    public ResponseEntity<Account> getAmount(@PathVariable("accountId") long accId) {
        Account account = accountService.getAccount(accId);
        return new ResponseEntity<Account>(account, HttpStatus.OK);
    }

}
