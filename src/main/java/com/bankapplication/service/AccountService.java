package com.bankapplication.service;

import com.bankapplication.dto.AccountDto;
import com.bankapplication.entity.Account;

public interface AccountService {
    String addAccount(AccountDto accountDto);

    Account getAccount(long id);

    void transaction();
}
