package edu.sc.csce740.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class StudentRecord {
    private Student student;
    private String college;
    private Term termBegan;
    private Term capstoneEnrolled;
    private String classStatus;
    private boolean gradAssistant;
    private boolean international;
    private String internationalStatus;
    private boolean resident;
    private boolean activeDuty;
    private boolean veteran;
    private boolean freeTuition;
    private String scholarship;
    private String studyAboard;
    private boolean nationalStudentExchange;
    private List<Course> courses;
    private List<Transaction> transactions;
}
