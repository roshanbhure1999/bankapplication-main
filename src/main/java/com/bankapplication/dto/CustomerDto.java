package com.bankapplication.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class CustomerDto {
    private long id;
    @NotBlank(message = "name.required ")
    private String name;
    @NotNull(message = "Mobile number is required")
    @Pattern(regexp = "[\\d]{10}", message = "Mobile number should 10 digit only")
    private String mobileNumber;
    @NotBlank(message = "address.required ")
    private String address;
    @NotBlank(message = "city.required ")
    private String city;
    @NotBlank(message = "state.required ")
    private String state;
    @NotBlank(message = "country.required ")
    private String country;
    @NotNull(message = "zipcode is required")
    @Pattern(regexp = "[\\d]{6}", message = "zipcode should 6 digit only")
    private String zipcode;
    @NotNull(message = "Aadhar number is required")
    @Pattern(regexp = "[\\d]{12}", message = "aadharNumber should 12 digit only")
    private String aadharNumber;
    @NotBlank(message = "PAN number is required")
    @Length(min = 10, max = 10, message = "panNumber should 10 digit only")
    private String panNumber;

}
