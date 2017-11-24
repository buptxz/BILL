package edu.sc.csce740.model;

import lombok.Getter;

@Getter
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

    public String getId() {
        return this.id;
    }
}
