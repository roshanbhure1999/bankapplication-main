package com.bankapplication.Controller;

import com.bankapplication.dto.TransactionDTO;
import com.bankapplication.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transferService;

    @GetMapping("/sevenDayTransaction")

    public ResponseEntity<List<TransactionDTO>> sevenDayTransaction(){
      List<TransactionDTO>   transaction=transferService.sevenDayTransaction();
      return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }
}
