package edu.sc.csce740.exception;

/**
 * Encounters when failed in editing existing {@link edu.sc.csce740.model.StudentRecord}
 */
public class IllegalRecordEditException extends Exception {
    public IllegalRecordEditException(String s) {
        super(s);
    }
}
