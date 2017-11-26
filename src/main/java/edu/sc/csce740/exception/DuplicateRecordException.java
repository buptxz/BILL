package edu.sc.csce740.exception;

/**
 * Encounters when there are duplicate {@link edu.sc.csce740.model.StudentRecord}.
 */
public class DuplicateRecordException extends RuntimeException {
    public DuplicateRecordException() {
        super("Duplicate record.");
    }
}
