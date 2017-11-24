package edu.sc.csce740.model;

import java.util.List;

public class Bill {
    private Student student;
    private String college;
    private String classStatus;
    private double balance;
    private List<Transaction> transactions;

    public Bill(Student student, String college, String classStatus) {
        this.student = student;
        this.college = college;
        this.classStatus = classStatus;
        generateBill(student);
    }

    public void generateBill(Student student) {

    }

}
