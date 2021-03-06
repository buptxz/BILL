package edu.sc.csce740.model;

import edu.sc.csce740.enums.StudyAbroad;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@SuppressWarnings("unused")
public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String emailAddress;
    private String addressStreet;
    private String addressCity;
    private String addressState;
    private String addressPostalCode;
}
