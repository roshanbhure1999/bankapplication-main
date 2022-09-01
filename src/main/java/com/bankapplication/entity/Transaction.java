package com.bankapplication.entity;

import com.bankapplication.constant.SavingOrCurrent;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String accountFrom;
    private String accountTo;
    @Enumerated(EnumType.STRING)
    private SavingOrCurrent accountType;
    private String ifscCode;
    private double amount;
    private String name;
    private LocalDate date;

}
