package com.bankapplication.service.impl;

import com.bankapplication.dto.TransactionDTO;
import com.bankapplication.entity.Transaction;

import com.bankapplication.repository.TransactionRepository;
import com.bankapplication.service.TransactionService;
import com.commonexception.exception.BankException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepository transactionRepository;

    @Override
    public List<TransactionDTO> sevenDayTransaction(long numberOfDays) {
        log.trace("sevenDayTransaction is processing");
        LocalDate date = LocalDate.now();
        LocalDate localDate = date.plusDays(numberOfDays);
        List<Transaction> bylocalTimeBetween = transactionRepository.findByDateBetween(date, localDate);
        if (bylocalTimeBetween.isEmpty()) {
            log.error("No any transaction in seven days ");
            throw new BankException("The no any transaction in between 7 Days", HttpStatus.BAD_REQUEST);
        }
        List<TransactionDTO> transactionDTOS = new ArrayList<>();
        for (Transaction transaction : bylocalTimeBetween) {
            TransactionDTO transactionDTO = entityToDto(transaction);
            transactionDTOS.add(transactionDTO);
        }
        log.debug("The seven days transaction successfully");
        return transactionDTOS;
    }

    private TransactionDTO entityToDto(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        BeanUtils.copyProperties(transaction, transactionDTO);
        return transactionDTO;
    }
}
