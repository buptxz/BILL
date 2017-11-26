package edu.sc.csce740.exception;

/**
 * Encounters when there's no {@link edu.sc.csce740.model.StudentRecord} found.
 */
public class NoFoundRecordException extends Exception {
    public NoFoundRecordException(String s) {
        super(s);
    }
}
