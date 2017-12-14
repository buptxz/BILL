package edu.sc.csce740.model;

import edu.sc.csce740.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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
    private StudyAbroad studyAbroad;
    private Boolean nationalStudentExchange;
    private Boolean outsideInsurance;
    private List<Course> courses;
    private List<Transaction> transactions;

    public StudentRecord(Student student) {
        this.student = student;
    }
}
