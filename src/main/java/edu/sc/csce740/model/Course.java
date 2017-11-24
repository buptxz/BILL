package edu.sc.csce740.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Course {
    private String name;
    private String id;
    private int numCredits;
    private boolean online;
}
