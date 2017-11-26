package edu.sc.csce740.exception;

/**
 * Exception when encounters invalid {@link edu.sc.csce740.model.StudentRecord}
 */
public class InvalidRecordException extends Exception {
    public InvalidRecordException(String s) {
        super(s);
    }
}


