package edu.sc.csce740.exception;

/**
 * Encounters when there's no {@link edu.sc.csce740.model.StudentRecord} found.
 */
public class NonExistentRecordException extends Exception {
    public NonExistentRecordException(String s) {
        super(s);
    }
}
