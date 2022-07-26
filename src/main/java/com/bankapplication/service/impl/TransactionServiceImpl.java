package com.bankapplication.service.impl;

import com.bankapplication.dto.TransactionDTO;
import com.bankapplication.entity.Transaction;
import com.bankapplication.exception.UserException;
import com.bankapplication.repository.TransactionRepository;
import com.bankapplication.service.TransactionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepository transactionRepository;

    @Override
    public List<TransactionDTO> sevenDayTransaction() {
        LocalDate date = LocalDate.now();
        LocalDate localDate = date.plusDays(7);
        List<Transaction> bylocalTimeBetween = transactionRepository.findByDateBetween(date, localDate);
        if (bylocalTimeBetween.isEmpty()) {
            throw new UserException("The no any transaction in between 7 Days", HttpStatus.BAD_REQUEST);
        }
        List<TransactionDTO> transactionDTOS = new ArrayList<>();
        for (Transaction transaction : bylocalTimeBetween) {
            TransactionDTO transactionDTO = entityToDto(transaction);
            transactionDTOS.add(transactionDTO);
        }
        return transactionDTOS;
    }

    private TransactionDTO entityToDto(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        BeanUtils.copyProperties(transaction, transactionDTO);
        return transactionDTO;
    }
}
