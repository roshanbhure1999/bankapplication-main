package com.bankapplication.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CustomerDto {

    private long cId;
    @NotEmpty(message = "name.required ")
    private String name;
    @NotEmpty(message = "mobileNumber.required ")
    @Size(min = 10, max = 10, message = "password must be 10 digit ")
    private String mobileNumber;
    @NotEmpty(message = "address.required ")
    private String address;
    @NotEmpty(message = "city.required ")
    private String city;
    @NotEmpty(message = "state.required ")
    private String state;
    @NotEmpty(message = "country.required ")
    private String country;
    @NotEmpty(message = "zipcode.required ")
    @Size(min = 10, max = 10, message = "password must be 10 digit ")
    private String zipcode;
    @NotEmpty(message = "aadharNumber.required ")
    @Size(min = 12, max = 12, message = "password must be 12 digit ")
    private String aadharNumber;
    @NotEmpty(message = "amount.required ")
    @Size(min = 10, max = 10, message = "password must be 10 digit ")
    private String panNumber;

}
