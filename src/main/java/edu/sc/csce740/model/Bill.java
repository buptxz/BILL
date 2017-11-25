package edu.sc.csce740.model;

import edu.sc.csce740.config.FeeConstant;
import edu.sc.csce740.enums.ClassStatus;
import edu.sc.csce740.enums.College;
import edu.sc.csce740.enums.Type;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Bill {
    private Student student;
    private College college;
    private ClassStatus classStatus;
    private double balance;
    private List<Transaction> transactions;

    public Bill(StudentRecord studentRecord) {
        this.student = studentRecord.getStudent();
        this.college = studentRecord.getCollege();
        this.classStatus = studentRecord.getClassStatus();
        this.transactions = new ArrayList<Transaction>();
    }

    public Bill(Bill currentBill, StudentRecord studentRecord, Date startDate, Date endDate) {
        this(studentRecord);
        if (startDate == null) {
            calculateCharge(studentRecord);
        } else {
            this.balance = currentBill.getBalance();
            for (Transaction transaction : studentRecord.getTransactions()) {
                if (transaction.getTransactionDate().isBetween(startDate, endDate)) {
                    transactions.add(transaction);
                }
            }
            for (Transaction transaction : currentBill.getTransactions()) {
                if (transaction.getTransactionDate().isBetween(startDate, endDate)) {
                    transactions.add(transaction);
                }
            }
        }
    }

    /**
     * Calculate charge based on the student information
     * @param studentRecord
     */
    public void calculateCharge(StudentRecord studentRecord) {
        int totalCredit = 0;
        for (Course course : studentRecord.getCourses()) {
            totalCredit += course.getNumCredits();
        }

        boolean isFullTime = totalCredit >= 12;

        if (studentRecord.getClassStatus() == ClassStatus.MASTERS ||
                studentRecord.getClassStatus() == ClassStatus.PHD) {
            // Graduate student
            if (studentRecord.getResident()) {
                if (isFullTime) {
                    addCharge(FeeConstant.GRADUATE_RESIDENT_TUITION_FULL_TIME
                            , FeeConstant.GRADUATE_RESIDENT_TUITION_FULL_TIME_NOTE);
                } else {
                    addCharge(FeeConstant.GRADUATE_RESIDENT_TUITION_PART_TIME * totalCredit
                            , FeeConstant.GRADUATE_RESIDENT_TUITION_PART_TIME_NOTE);
                }
            } else {
                if (isFullTime) {
                    addCharge(FeeConstant.GRADUATE_NONRESIDENT_TUITION_FULL_TIME
                            , FeeConstant.GRADUATE_NONRESIDENT_TUITION_FULL_TIME_NOTE);
                } else {
                    addCharge(FeeConstant.GRADUATE_NONRESIDENT_TUITION_PART_TIME * totalCredit
                            , FeeConstant.GRADUATE_NONRESIDENT_TUITION_PART_TIME_NOTE);
                }
            }

        } else {

        }

        if (isFullTime) {
            addCharge(FeeConstant.TECHNOLOGY_FEE_FULL_TIME
                    , FeeConstant.TECHNOLOGY_FEE_FULL_TIME_NOTE);
        } else {
            addCharge(FeeConstant.TECHNOLOGY_FEE_PART_TIME
                    , FeeConstant.TECHNOLOGY_FEE_PART_TIME_NOTE);
        }

    }

    /**
     * Add charge to the transaction history
     * @param amount
     * @param note
     */
    private void addCharge(double amount, String note) {
        this.balance += amount;
        this.transactions.add(new Transaction(Type.CHARGE, amount, note));
    }

    /**
     * Make a payment
     * @param amount
     * @param note
     * @throws Exception
     */
    public void makePayment(BigDecimal amount, String note) throws Exception {
        BigDecimal balanceBD = new BigDecimal(this.balance);
        if (amount.compareTo(balanceBD) == 1) {
            throw new Exception();
        } else {
            balanceBD = balanceBD.subtract(amount);
            this.balance = balanceBD.doubleValue();
            transactions.add(new Transaction(Type.PAYMENT, amount.doubleValue(), note));
        }

    }
//
//    public String toString() {
//        String output = "";
//        output += "ID: " + this.getStudent().getId() + "\n";
//        output += "Balance: " + this.getBalance() + "\n\n";
//        for (Transaction transaction : transactions) {
//            output += transaction.toString() + "\n";
//        }
//        return output;
//    }
}
