package com.bankapplication.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class BankDto {


    @NotEmpty(message = "bankName.required ")
    private String bankName;
    @Length(min = 11, max = 11, message = "ifscCode must be 14 digit ")
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
    @NotNull(message = "zipcode is required")
    @Pattern(regexp = "[\\d]{6}", message = "zipcode should 6 digit only")
    private String zipcode;

}
