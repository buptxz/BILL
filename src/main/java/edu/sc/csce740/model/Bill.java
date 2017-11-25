package edu.sc.csce740.model;

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
//        final double UNDERGRADUATE_RESIDENT_TUITION_FULL_TIME = 5727;
//        final double UNDERGRADUATE_RESIDENT_TUITION_PART_TIME = 477.25;
//        final double UNDERGRADUATE_NONRESIDENT_TUITION_FULL_TIME = 5727;
//        final double UNDERGRADUATE_NONRESIDENT_TUITION_PART_TIME = 1286.75;
//        final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_WOODROW_TUITION_FULL_TIME = 8502;
//        final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_WOODROW_TUITION_PART_TIME = 708.5;
//        final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_GENERAL_TUITION_FULL_TIME = 5727;
//        final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_GENERAL_TUITION_PART_TIME = 477.25;
//        final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_ATHLETICS_TUITION_FULL_TIME = 8502;
//        final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_ATHLETICS_TUITION_PART_TIME = 708.5;
//        final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_SIMS_TUITION_FULL_TIME = 10965;
//        final double UNDERGRADUATE_NONRESIDENT_SCHOLARSHIP_SIMS_TUITION_PART_TIME = 913.75;
//        final double UNDERGRADUATE_ACTIVE_DUTY_MILITARY_TUITION_FULL_TIME = 3351;
//        final double UNDERGRADUATE_ACTIVE_DUTY_MILITARY_TUITION_PART_TIME = 279.25;
        final double GRADUATE_RESIDENT_TUITION_FULL_TIME = 6399;
        final double GRADUATE_RESIDENT_TUITION_PART_TIME = 533.25;
        final double GRADUATE_NONRESIDENT_TUITION_FULL_TIME = 13704;
        final double GRADUATE_NONRESIDENT_TUITION_PART_TIME = 1142;
        final double TECHNOLOGY_FEE_FULL_TIME = 200;
        final double TECHNOLOGY_FEE_PART_TIME = 17;
//        final double STUDY_ABROAD = 150;
//        final double COHORT_STUDY_ABROAD = 300;
//        final double MATRICULATION_FEE = 80;

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
                    addCharge(GRADUATE_RESIDENT_TUITION_FULL_TIME, "GRADUATE_RESIDENT_TUITION_FULL_TIME");
                } else {
                    addCharge(GRADUATE_RESIDENT_TUITION_PART_TIME * totalCredit, "GRADUATE_RESIDENT_TUITION_PART_TIME");
                }
            } else {
                if (isFullTime) {
                    addCharge(GRADUATE_NONRESIDENT_TUITION_FULL_TIME, "GRADUATE_NONRESIDENT_TUITION_FULL_TIME");
                } else {
                    addCharge(GRADUATE_NONRESIDENT_TUITION_PART_TIME * totalCredit, "GRADUATE_NONRESIDENT_TUITION_PART_TIME");
                }
            }

        } else {

        }

        if (isFullTime) {
            addCharge(TECHNOLOGY_FEE_FULL_TIME, "TECHNOLOGY_FEE_FULL_TIME");
        } else {
            addCharge(TECHNOLOGY_FEE_PART_TIME, "TECHNOLOGY_FEE_PART_TIME");
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
