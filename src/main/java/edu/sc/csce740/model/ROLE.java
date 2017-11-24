package edu.sc.csce740.model;

public enum ROLE {
    STUDENT("STUDENT"),
    ADMIN("ADMIN");

    private final String name;

    private ROLE(String name) {
        this.name = name;
    }
}
