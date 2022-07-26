package com.bankapplication.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "account")
public class Account implements Serializable {

    @Column(name = "accountNumber")
    private String accountNumber;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "acc_id")
    private long accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "accountType")
    private SavingOrCurrent accountType;

    @Column(name = "isBlocked")
    private boolean isBlocked;

    @Column(name = "amount")
    private double amount;

    private String ifscCode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cust_Id", referencedColumnName = "cId")
    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_Id", referencedColumnName = "bId")
    private Bank bank;


}
