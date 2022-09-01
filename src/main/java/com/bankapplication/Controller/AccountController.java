package com.bankapplication.Controller;

import com.bankapplication.constantUrl.Url;
import com.bankapplication.dto.AccountDto;
import com.bankapplication.entity.Account;
import com.bankapplication.service.AccountService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@RestController
@RequestMapping(path = Url.ACCOUNT)
@Validated
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
    public ResponseEntity<String> createAccount(@RequestBody @Valid  AccountDto accountDto) {
        String s = accountService.addAccount(accountDto);
        return new ResponseEntity<String>(s, HttpStatus.CREATED);
    }

    @GetMapping(path = Url.ID)
    public ResponseEntity<Account> getAmount(@PathVariable("id") long id) {
        Account account = accountService.getAccount(id);
        return new ResponseEntity<Account>(account, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> listAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping(Url.ACCOUNT_NUMBER)
    public ResponseEntity<AccountDto> findByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
        return ResponseEntity.ok(accountService.findByAccountNumber(accountNumber));
    }

    @GetMapping(Url.LIKE)
    public ResponseEntity<List<AccountDto>> search(@PathVariable String content){
        return ResponseEntity.ok(accountService.search(content));

    }
    @GetMapping("searchBlockAccount/{content}")
    public ResponseEntity<List<AccountDto>> searchBlockAccount(@PathVariable int content){
        return ResponseEntity.ok(accountService.searchBlockAccount(content));
    }
    @DeleteMapping(Url.ID)
    public ResponseEntity<String> deleteById(@PathVariable long id){
        return ResponseEntity.ok(accountService.deleteById(id));
    }

    @PutMapping(Url.UPDATE)
    public ResponseEntity<String> updateAccount(@Valid @RequestBody AccountDto accountDto) {
        String s = accountService.updateAccount(accountDto);
        return new ResponseEntity<String>(s, HttpStatus.OK);
    }

}
