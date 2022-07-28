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

    /**
     *
     * @param accountDto
     * @return
     * @apiNote This Api create for save the account as per Customer details and Bank Details
     *           And One more thing is imp that One person  open only two account one is saving and current
     * @serialData
     */
    @PostMapping
    public ResponseEntity<String> createAccount(@Valid @RequestBody AccountDto accountDto) {
        String s = accountService.addAccount(accountDto);
        return new ResponseEntity<String>(s, HttpStatus.CREATED);
    }

    /**
     *
     * @param accId
     * @return
     * @apiNote This Api create for find by account number
     *      if the account number is  not valid then throw the exception
     *
     * @exceptionThe account id is invalid for this given id  HttpStatus.BAD_REQUEST
     */
    @GetMapping(path = "/{accId}")
    public ResponseEntity<Account> getAmount(@PathVariable("accountId") long accId) {
        Account account = accountService.getAccount(accId);
        return new ResponseEntity<Account>(account, HttpStatus.OK);
    }

}
