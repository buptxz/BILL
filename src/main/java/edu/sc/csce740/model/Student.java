package edu.sc.csce740.model;

import edu.sc.csce740.enums.StudyAbroad;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
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

//    public boolean equals(Student otherStudent) {
//        return otherStudent != null && this.id.equals(otherStudent.getId()) && this.firstName
//    }
}
