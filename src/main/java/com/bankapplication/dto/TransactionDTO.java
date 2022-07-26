package com.bankapplication.dto;

import com.bankapplication.entity.SavingOrCurrent;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Data
public class TransactionDTO {
    @NotEmpty(message = "accountNumberFrom.required ")
    @Size(min = 14,max = 14, message = "password must be 14 digit ")
    private String accountNumberFrom;
    @NotEmpty(message = "accountNumberTo.required ")
    @Size(min = 14,max = 14, message = "password must be 14 digit ")
    private String accountNumberTo;
    @NotEmpty(message = "amount.required ")
    private double amount;
    @NotEmpty(message = "ifscCode.required ")
    @Size(min = 12,max = 12, message = "ifscCode must be 12 digit ")
    private String ifscCode;
    @NotEmpty(message = "name.required ")
    private String name;
    @NotEmpty(message = "accountType.required ")
    private SavingOrCurrent accountType;
    @NotEmpty(message = "date.required ")
    private LocalTime date;
}
