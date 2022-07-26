package com.bankapplication.service;

import com.bankapplication.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    List<TransactionDTO> sevenDayTransaction();
}
