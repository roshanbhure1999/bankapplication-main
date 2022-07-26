package com.bankapplication.constant;

public enum AccountBalance {
    SAVING(5000.0),
    CURRENT(10000.0),
    Interest(0.5),
    Total(100.0);
    private final Double balance;

    AccountBalance(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }
}
