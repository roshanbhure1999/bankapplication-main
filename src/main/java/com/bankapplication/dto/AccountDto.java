package com.bankapplication.dto;

import com.bankapplication.constant.SavingOrCurrent;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AccountDto implements Serializable {

    @Length(min = 14, max = 14, message = "accountNumber must be 14 digit ")
    private String accountNumber;
    @NotEmpty(message = "accountType.required ")
    private SavingOrCurrent accountType;
    @NotNull(message = "Amount can not be empty")
    private double amount;
    @NotEmpty(message = "customerId.required ")
    private long customerId;
    @NotEmpty(message = "bankId.required ")
    private long bankId;
    @Length(min = 11, max = 11, message = "ifscCode should 11 character only")
    private String ifscCode;
}
