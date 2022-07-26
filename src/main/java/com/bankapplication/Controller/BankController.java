package com.bankapplication.Controller;

import com.bankapplication.dto.BankDto;
import com.bankapplication.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/bank")
public class BankController {
    @Autowired
    private BankService bankService;

    @PostMapping
    public ResponseEntity<String> saveBank(@Valid @RequestBody BankDto bankDto) {
        String s = bankService.addBank(bankDto);
        return new ResponseEntity<String>(s, HttpStatus.CREATED);
    }


}
