package com.bankapplication.service.impl;

import com.bankapplication.dto.BankDto;
import com.bankapplication.entity.Bank;
import com.bankapplication.repository.BankRepository;
import com.bankapplication.service.BankService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;

    @Override
    public String addBank(BankDto bankdto) {
        /*Bank byIfscCode = bankRepository.findBYIfscCode(bankdto.getIfscCode());*/
        if (bankdto.getIfscCode().length() != 11) {
            return "invalid IFSCCode";
        }
        bankRepository.save(dtoToEntity(bankdto));
        return " Done";
    }

    private Bank dtoToEntity(BankDto bankdto) {
        Bank bank = new Bank();
        bank.setBankName(bankdto.getBankName());
        bank.setIfscCode(bankdto.getIfscCode());
        bank.setBranchName(bankdto.getBranchName());
        bank.setAddress(bankdto.getAddress());
        bank.setCity(bankdto.getCity());
        bank.setState(bankdto.getState());
        bank.setCountry(bankdto.getCountry());
        bank.setZipcode(bankdto.getZipcode());
        bank.setBId(bankdto.getBId());
        bank.setCity(bankdto.getCity());

        return bank;
    }
}
