package com.bankapplication.Controller;

import com.bankapplication.constantUrl.Url;
import com.bankapplication.dto.AccountDto;
import com.bankapplication.dto.BankDto;
import com.bankapplication.dto.CustomerDto;
import com.bankapplication.service.BankService;
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
@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping(path = Url.BANK)
public class BankController {

    private final BankService bankService;

    /**
     *
     * @param bankDto
     * @return
     */
    @PostMapping
    public ResponseEntity<String> saveBank(@Valid @RequestBody BankDto bankDto) {
        String s = bankService.addBank(bankDto);
        return new ResponseEntity<String>(s, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<BankDto>> findAll() {
        return ResponseEntity.ok(bankService.findAll());
    }

    @GetMapping(Url.Id)
    public ResponseEntity<BankDto> findBankById(@PathVariable Integer id) {
        return ResponseEntity.ok(bankService.findById(id));
    }
    @GetMapping(Url.LIKE)
    public ResponseEntity<List<BankDto>> search(@PathVariable String content){
        return ResponseEntity.ok(bankService.search(content));
    }
    @DeleteMapping(Url.Id)
    public ResponseEntity<String> deleteBank(@PathVariable Long id){
        return ResponseEntity.ok(bankService.deleteBank(id));
    }
    @PutMapping(Url.UPDATE)
    public ResponseEntity<String> updateCustomer(@Valid @RequestBody BankDto bankDto) {
        String s = bankService.updateCustomer(bankDto);
        return new ResponseEntity<String>(s, HttpStatus.OK);
    }
    @GetMapping(Url.BANK+Url.Id)
    public ResponseEntity<List<AccountDto>> getAccountFormBankId(@PathVariable("id") long id) {
        return ResponseEntity.ok(bankService.getAccountFormBankId(id));
    }


}

