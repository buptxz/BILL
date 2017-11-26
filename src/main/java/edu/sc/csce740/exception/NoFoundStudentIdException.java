package edu.sc.csce740.exception;

/**
 * Encounters when no student id is found.
 */
public class NoFoundStudentIdException extends Exception {
    public NoFoundStudentIdException(String s) {
        super(s);
    }
}
