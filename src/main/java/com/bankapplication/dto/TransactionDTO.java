package com.bankapplication.dto;

import com.bankapplication.constant.SavingOrCurrent;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Data
public class TransactionDTO {
    @NotNull(message = "From Account can not be empty")
    @Size(min = 14, max = 14, message = "password must be 14 digit ")
    private String accountNumberFrom;
    @NotNull(message = "From Account can not be empty")
    @Size(min = 14, max = 14, message = "password must be 14 digit ")
    private String accountNumberTo;
    @NotNull(message = "Amount can not be empty")
    private double amount;
    @Length(min = 11, max = 11, message = "ifscCode should 11 character only")
    private String ifscCode;
    @NotBlank(message = "name.required ")
    private String name;
    @NotEmpty(message = "accountType.required ")
    private SavingOrCurrent accountType;
    @NotEmpty(message = "date.required ")
    private LocalTime date;
}
