package edu.sc.csce740.model;

import edu.sc.csce740.enums.Type;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Calendar;

@Getter
@ToString(exclude="transactionDate")
public class Transaction {
    private Type type;
    private Date transactionDate;
    private double amount;
    private String note;

    public Transaction(Type type, double amount, String note) {
        Calendar now = Calendar.getInstance();
        transactionDate = new Date(now.get(Calendar.MONTH) + 1,
                now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.YEAR));
        this.type = type;
        this.amount = amount;
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        Transaction otherTransaction = (Transaction) o;
        return otherTransaction != null && this.type == otherTransaction.getType() &&
                this.amount == otherTransaction.getAmount() &&
                ((this.note == null && otherTransaction.getNote() == null) || this.note.equals(otherTransaction.getNote()));
    }
}
