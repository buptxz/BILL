package edu.sc.csce740.exception;

/**
 * Exception when loading student records to the system.
 */
public class LoadRecordException extends Exception {
    public LoadRecordException(String s) {
        super(s);
    }
}
