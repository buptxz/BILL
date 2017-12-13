package edu.sc.csce740.model;


import edu.sc.csce740.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class Term {
    private Semester semester;
    private int year;
}
