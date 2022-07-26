package com.bankapplication.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String  accountFrom;
    private String accountTo;
    private SavingOrCurrent accountType;
    private String ifscCode;
    private String name;
    private LocalDate date;

}
