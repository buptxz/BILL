package edu.sc.csce740.exception;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException() {
        super("Invalid date format.");
    }
}
