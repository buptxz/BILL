package edu.sc.csce740.model;

import edu.sc.csce740.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Calendar;

@Getter
@AllArgsConstructor
@ToString
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

//    @Override
//    public String toString() {
//        return "Type: " + type + "\n" +
//                "Date: " + transactionDate.toString() + "\n" +
//                "Amount: " + amount + "\n" +
//                "Note: " + note + "\n";
//    }
}
