package edu.sc.csce740.exception;

/**
 * Encounters when no student id is found.
 */
public class NonExistentStudentIdException extends Exception {
    public NonExistentStudentIdException(String s) {
        super(s);
    }
}
