package com.bankapplication.service;

import com.bankapplication.dto.AccountDto;
import com.bankapplication.dto.BankDto;

import java.util.List;

public interface BankService {
    String addBank(BankDto bankdto);
    BankDto findById(long id);
    List<BankDto> findAll();

    List<BankDto> search(String content);

    String deleteBank(Long bankId);

    String updateCustomer(BankDto bankDto);

    List<AccountDto> getAccountFormBankId(long id);
}
