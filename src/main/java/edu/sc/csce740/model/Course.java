package edu.sc.csce740.model;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class Course {
    private String name;
    private String id;
    private int numCredits;
    private Boolean online;
}
