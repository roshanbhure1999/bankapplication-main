package com.bankapplication.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "bank")
@Data
public class Bank implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bId")
    private long bId;

    @Column(name = "bankName")
    private String bankName;

    @Column(name = "ifscCode")
    private String ifscCode;

    @Column(name = "branchName")
    private String branchName;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "zipcode")
    private String zipcode;

}
