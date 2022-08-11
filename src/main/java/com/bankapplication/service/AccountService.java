package com.bankapplication.service;

import com.bankapplication.dto.AccountDto;
import com.bankapplication.entity.Account;

import java.util.List;

public interface AccountService {
    String addAccount(AccountDto accountDto);

    Account getAccount(long id);

    void transaction();
    List<AccountDto> getAllAccounts();

    AccountDto findByAccountNumber(String accountNumber);

    List<AccountDto> search(String content);

    List<AccountDto> searchBlockAccount(int content);

    String deleteById(long id);

    String updateAccount(AccountDto accountDto);

}
