package edu.sc.csce740.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class StudentRecord {
    private Student student;
    private College college;
    private Term termBegan;
    private Term capstoneEnrolled;
    private ClassStatus classStatus;
    private Boolean gradAssistant;
    private Boolean international;
    private InternationalStatus internationalStatus;
    private Boolean resident;
    private Boolean activeDuty;
    private Boolean veteran;
    private Boolean freeTuition;
    private Scholarship scholarship;
    private StudentRecord studyAboard;
    private Boolean nationalStudentExchange;
    private List<Course> courses;
    private List<Transaction> transactions;
}
