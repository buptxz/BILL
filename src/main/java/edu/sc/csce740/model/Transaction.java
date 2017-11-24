package edu.sc.csce740.model;

import lombok.Getter;

@Getter
public class Transaction {
    private String type;
    private Date transactionDate;
    private double amount;
    private String note;
}
