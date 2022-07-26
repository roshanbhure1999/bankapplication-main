package com.bankapplication.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class BankDto {
    private long bId;
    @NotEmpty(message = "bankName.required ")
    private String bankName;
    @NotEmpty(message = "amount.required ")
    @Size(min = 14, max = 14, message = "password must be 14 digit ")
    private String ifscCode;
    @NotEmpty(message = "branchName.required ")
    private String branchName;
    @NotEmpty(message = "amount.required ")
    @Size(min = 3, max = 100, message = "password must be min of 3 character and max of 100 character")
    private String address;
    @NotEmpty(message = "amount.required ")
    @Size(min = 3, max = 15, message = "password must be min of 3 character and max of 15 character")
    private String city;
    @NotEmpty(message = "amount.required ")
    @Size(min = 3, max = 14, message = "password must be min of 3 character and max of 14 character")
    private String state;
    @NotEmpty(message = "country.required ")
    @Size(min = 3, max = 14, message = "password must be min of 3 character and max of 14 character")
    private String country;
    @NotEmpty(message = "amount.required ")
    @Size(min = 6, max = 6, message = "zipcode must be 6 digit ")
    private String zipcode;

}
