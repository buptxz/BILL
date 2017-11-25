package edu.sc.csce740.model;

import edu.sc.csce740.enums.ClassStatus;
import edu.sc.csce740.enums.College;
import edu.sc.csce740.enums.InternationalStatus;
import edu.sc.csce740.enums.Scholarship;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
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
    private StudentRecord studyAboard;
    private Boolean nationalStudentExchange;
    private List<Course> courses;
    private List<Transaction> transactions;
}
