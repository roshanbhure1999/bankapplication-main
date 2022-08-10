package com.bankapplication.service.impl;

import com.bankapplication.dto.CustomerDto;
import com.bankapplication.entity.Customer;
import com.bankapplication.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {


    @InjectMocks
    CustomerServiceImpl customerService;

    @Mock
    CustomerRepository customerRepository;

    Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
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
           when(customerRepository.save(any())).thenReturn(customer);
        CustomerDto customerDto = entityToDto();
        assertEquals("Done", customerService.addCustomer(customerDto));
    }

    @Test
    void transferMoney() {
    }

    @Test
    void findById() {
     //   Mockito.when(customerRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(customer));
        CustomerDto byId = customerService.findById(252L);
        Assertions.assertEquals(byId.getName(), customer.getName());
    }

    @Test
    void findAll() {
    }

    @Test
    void deleteById() {
    }
}