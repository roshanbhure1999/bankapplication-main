package com.bankapplication.Controller;

import com.bankapplication.dto.TransactionDTO;
import com.bankapplication.service.TransactionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@RestController
public class TransactionController {


    private final TransactionService transferService;

    @GetMapping("/sevenDayTransaction/{numberOfDays}")

    public ResponseEntity<List<TransactionDTO>> sevenDayTransaction(@PathVariable int numberOfDays) {
        List<TransactionDTO> transaction = transferService.sevenDayTransaction(numberOfDays);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }
}
