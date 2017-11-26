package edu.sc.csce740.exception;

public class DuplicateRecordException extends RuntimeException {
    public DuplicateRecordException() {
        super("Duplicate record.");
    }
}
