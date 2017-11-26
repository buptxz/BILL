package edu.sc.csce740.exception;

/**
 * Exception when encounters invalid payment.
 */
public class InvalidPaymentException extends Exception {
    public InvalidPaymentException(String s) {
        super(s);
    }
}
