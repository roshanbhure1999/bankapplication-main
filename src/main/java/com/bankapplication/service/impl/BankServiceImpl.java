package com.bankapplication.service.impl;

import com.bankapplication.dto.BankDto;
import com.bankapplication.entity.Bank;
import com.bankapplication.exception.BankException;
import com.bankapplication.repository.BankRepository;
import com.bankapplication.service.BankService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
@Service
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;

    @Override
    public String addBank(BankDto bankdto) {
        if (bankdto.getIfscCode().length() != 11) {
            log.error("Invalid IFSC_Code plz enter 11 digit::{}", bankdto.getIfscCode());
            return "invalid IFSCCode";
        }
        Bank bank = bankRepository.findByIfscCode(bankdto.getIfscCode());
        if (!Objects.isNull(bank)) {
            log.error("Bank Already  register for this given ifsc code ::{}", bankdto.getIfscCode());
            throw new BankException("Bank Already  register for this given ifsc code " + bankdto.getIfscCode(), HttpStatus.BAD_REQUEST);
        }

        bankRepository.save(dtoToEntity(bankdto));
        log.debug("Bank Saves Successfully");
        return " Done";
    }

    private Bank dtoToEntity(BankDto bankdto) {
        log.trace("converting dto to entity And return bank");
        Bank bank = new Bank();
        BeanUtils.copyProperties(bankdto, bank);
        log.debug("BankDto To convert Bank ..");
        return bank;
    }
}
