package com.bankapplication.dto;

import com.bankapplication.entity.SavingOrCurrent;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class AccountDto implements Serializable {
    private long accId;
    @NotEmpty(message = "amount.required ")
    @Size(min = 14, max = 14, message = "password must be 14 digit ")
    private String accountNumber;
    @NotEmpty(message = "accountType.required ")
    private SavingOrCurrent accountType;
    @NotEmpty(message = "amount.required ")
    private double amount;
    @NotEmpty(message = "customerId.required ")
    private long customerId;
    @NotEmpty(message = "bankId.required ")
    private long bankId;
    @Size(min = 12, max = 12, message = "ifscCode must be 12 digit ")
    private String ifscCode;
}
