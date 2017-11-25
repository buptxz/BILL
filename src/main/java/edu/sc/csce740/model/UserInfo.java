package edu.sc.csce740.model;

import edu.sc.csce740.enums.College;
import edu.sc.csce740.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfo {
    private String id;
    private String firstName;
    private String lastName;
    private Role role;
    private College college;
}