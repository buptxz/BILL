package edu.sc.csce740.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Transaction {
    private String type;
    private Date transactionDate;
    private double amount;
    private String note;
}
