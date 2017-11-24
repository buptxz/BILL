package edu.sc.csce740.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Permission {
    private String id;
    private String firstName;
    private String lastName;
    private String role;
    private String college;
}
