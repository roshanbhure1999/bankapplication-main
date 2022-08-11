package com.bankapplication.service.impl;

import com.bankapplication.dto.CustomerDto;
import com.bankapplication.entity.Customer;
import com.bankapplication.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    Customer customer;

    @BeforeEach
    void setUp() {
       // MockitoAnnotations.initMocks(this);
       // this.customerService = new CustomerServiceImpl(this.customerRepository);

    }

    private CustomerDto entityToDto(){
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }

    @Test
    void addCustomer() {

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
           when(customerRepository.findByPanNumberOrAadharNumber(anyString(),anyString())).thenReturn(new Customer());
           when(customerRepository.save(any(Customer.class))).thenReturn(customer);
       // doNothing().when(customerRepository.save(any()));
       // CustomerDto customerDto = ;
        assertEquals("Done", customerService.addCustomer(entityToDto()));
      //  verify(customerRepository).save(customer);
    }

    @Test
    void transferMoney() {
    }

    @Test
    void findById() {

    //  when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        CustomerDto byId = customerService.findById(252L);
       // assertEquals(byId.getName(), customer.getName());

    }

    @Test
    void findAll() {
        customerService.findAll();
        verify(customerRepository).findAll();

    }

    @Test
    void deleteById() {
    }
}