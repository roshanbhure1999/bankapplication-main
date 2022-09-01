package com.bankapplication.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bankapplication.dto.BankDto;
import com.bankapplication.repository.AccountRepository;
import com.bankapplication.repository.BankRepository;
import com.commonexception.exception.BankException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {BankServiceImpl.class})
@ExtendWith(SpringExtension.class)
class BankServiceImplTest {
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private BankRepository bankRepository;

    @Autowired
    private BankServiceImpl bankServiceImpl;

    /**
     * Method under test: {@link BankServiceImpl#addBank(BankDto)}
     */
    @Test
    void testAddBank() {
        BankDto bankDto = new BankDto();
        bankDto.setAddress("42 Main St");
        bankDto.setBankName("Bank Name");
        bankDto.setBranchName("janedoe/featurebranch");
        bankDto.setCity("Oxford");
        bankDto.setCountry("GB");
        bankDto.setId(123L);
        bankDto.setIfscCode("Ifsc Code");
        bankDto.setState("MD");
        bankDto.setZipcode("21654");
        assertThrows(BankException.class, () -> bankServiceImpl.addBank(bankDto));
    }

    /**
     * Method under test: {@link BankServiceImpl#addBank(BankDto)}
     */
    @Test
    void testAddBank2() {
        BankDto bankDto = mock(BankDto.class);
        when(bankDto.getIfscCode()).thenReturn("Ifsc Code");
        doNothing().when(bankDto).setAddress((String) any());
        doNothing().when(bankDto).setBankName((String) any());
        doNothing().when(bankDto).setBranchName((String) any());
        doNothing().when(bankDto).setCity((String) any());
        doNothing().when(bankDto).setCountry((String) any());
        doNothing().when(bankDto).setId(anyLong());
        doNothing().when(bankDto).setIfscCode((String) any());
        doNothing().when(bankDto).setState((String) any());
        doNothing().when(bankDto).setZipcode((String) any());
        bankDto.setAddress("42 Main St");
        bankDto.setBankName("Bank Name");
        bankDto.setBranchName("janedoe/featurebranch");
        bankDto.setCity("Oxford");
        bankDto.setCountry("GB");
        bankDto.setId(123L);
        bankDto.setIfscCode("Ifsc Code");
        bankDto.setState("MD");
        bankDto.setZipcode("21654");
        assertThrows(BankException.class, () -> bankServiceImpl.addBank(bankDto));
        verify(bankDto, atLeast(1)).getIfscCode();
        verify(bankDto).setAddress((String) any());
        verify(bankDto).setBankName((String) any());
        verify(bankDto).setBranchName((String) any());
        verify(bankDto).setCity((String) any());
        verify(bankDto).setCountry((String) any());
        verify(bankDto).setId(anyLong());
        verify(bankDto).setIfscCode((String) any());
        verify(bankDto).setState((String) any());
        verify(bankDto).setZipcode((String) any());
    }
}

