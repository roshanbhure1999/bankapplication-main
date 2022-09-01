package com.bankapplication.service.impl;

import com.bankapplication.dto.CustomerDto;
import com.bankapplication.entity.Customer;
import com.bankapplication.repository.AccountRepository;
import com.bankapplication.repository.CustomerRepository;
import com.commonexception.exception.BankException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CustomerServiceImplTest {


    @InjectMocks
    CustomerServiceImpl customerService;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    AccountRepository accountRepository;


    Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCId(1);
        customer.setName("Roshan Raju Bhure");
        customer.setAddress("nagpur");
        customer.setMobileNumber("7887766478");
        customer.setCity("nagpur");
        customer.setState("Maharastra");
        customer.setCountry("India");
        customer.setZipcode("441604");
        customer.setAadharNumber("830277743123");
        customer.setPanNumber("QWERT02330");

    }

    private CustomerDto entityToDto(){
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }

    @Test
    void addCustomer() {


           when(customerRepository.findByPanNumberOrAadharNumber(anyString(),anyString())).thenReturn(new Customer());
           when(customerRepository.save(any(Customer.class))).thenReturn(customer);
       // doNothing().when(customerRepository.save(any()));
       // CustomerDto customerDto = ;
        assertEquals("Done", customerService.addCustomer(entityToDto()));
      //  verify(customerRepository).save(customer);
    }



    @Test
    void findById() {

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        CustomerDto byId = customerService.findById(1L);
        assertEquals(customer.getName(),byId.getName());
         }

    @Test
    void findAll() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));
        assertEquals(List.of(customer).size(), customerService.findAll().size());
    }
    @Test
    void findAllInvalid() {
        List<Customer> customerDtos = new ArrayList<>();
      when(customerRepository.findAll()).thenReturn(customerDtos);
        doThrow(new BankException("", HttpStatus.NO_CONTENT)).when(customerRepository).findAll();

    }

    @Test
    void deleteById() {
      //  when(accountRepository.findByCustomer(customer)).thenReturn();


        assertEquals(String.format("The customer delete successfully for this given id %d", customer.getCId()),customerService.deleteById(customer.getCId()));
    }
}