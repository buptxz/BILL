package edu.sc.csce740.exception;

/**
 * Encounters when no student id is found.
 */
public class StudentIdNotFoundException extends Exception {
    public StudentIdNotFoundException(String s) {
        super(s);
    }
}
