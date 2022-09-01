package com.bankapplication.service.impl;

import com.bankapplication.dto.AccountDto;
import com.bankapplication.dto.BankDto;
import com.bankapplication.entity.Account;
import com.bankapplication.entity.Bank;

import com.bankapplication.repository.AccountRepository;
import com.bankapplication.repository.BankRepository;
import com.bankapplication.service.BankService;
import com.commonexception.exception.BankException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
@Service
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;

    private final AccountRepository accountRepository;

    @Override
    public String addBank(BankDto bankdto) {
        if (bankdto.getIfscCode().length() != 11) {
            log.error("Invalid IFSC_Code plz enter 11 digit::{}", bankdto.getIfscCode());
            throw new BankException("Invalid IFSC_Code plz enter 11 digit", HttpStatus.BAD_REQUEST);
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

    public BankDto findById(long id) {
        BankDto bank = bankRepository.findById(id).map(this::toBankDTO).orElseThrow(() -> new BankException("", HttpStatus.NO_CONTENT));
        return bank;
    }


    public List<BankDto> findAll() {
        List<BankDto> banks = bankRepository.findAll().stream().map(this::toBankDTO).collect(Collectors.toList());
        if (!banks.isEmpty()) {
            return banks;
        } else throw new BankException("", HttpStatus.NO_CONTENT);
    }

    @Override
    public List<BankDto> search(String content) {
        List<BankDto> bank = bankRepository.findByTitleContent("%" + content + "%").stream().map(this::toBankDTO).collect(Collectors.toList());
        if (!bank.isEmpty()) {
            return bank;
        } else throw new BankException("", HttpStatus.NO_CONTENT);

    }

    @Override
    public String deleteBank(Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow(() -> new BankException("bank not present for this given id " + bankId, HttpStatus.NOT_FOUND));
        List<Account> Bank = accountRepository.findByBank(bank);
        if (Bank.isEmpty()) {
            bankRepository.deleteById(bankId);
            return "The bank delete successfully for this given id " + bankId;
        }
        throw new BankException("the bank can't delete because the bank link to the account ", HttpStatus.BAD_REQUEST);

    }

    @Override
    public String updateCustomer(BankDto bankDto) {
        Optional<Bank> bank = bankRepository.findById(bankDto.getId());
        if (Objects.isNull(bank)) {
            throw new BankException("the bank not found for this given bankId" + bankDto.getId(), HttpStatus.BAD_REQUEST);
        }
        Bank bankUpdate = bank.get();
        bankUpdate.setBankName(bankUpdate.getBankName());
        bankUpdate.setIfscCode(bankUpdate.getIfscCode());
        bankUpdate.setAddress(bankDto.getAddress());
        bankUpdate.setCity(bankDto.getCity());
        bankUpdate.setCountry(bankDto.getCountry());
        bankUpdate.setZipcode(bankDto.getZipcode());
        bankUpdate.setState(bankDto.getState());
        bankRepository.save(bankUpdate);
        return "The bank update successfully";

    }


    @Override
    public List<AccountDto> getAccountFormBankId(long id) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new BankException("the bank not found for this given bankId", HttpStatus.BAD_REQUEST));

        List<Account> byBank = accountRepository.findByBank(bank);
        return byBank.stream().map(this::toAccountDto).collect(Collectors.toList());

    }

    private BankDto toBankDTO(Bank bank) {
        BankDto bankDTO = new BankDto();
        BeanUtils.copyProperties(bank, bankDTO);
        bankDTO.setId(bank.getBId());
        return bankDTO;
    }

    private AccountDto toAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();
        BeanUtils.copyProperties(account, accountDto);
        accountDto.setCustomerId(account.getCustomer().getCId());
        accountDto.setBankId(account.getBank().getBId());
        return accountDto;
    }


}
