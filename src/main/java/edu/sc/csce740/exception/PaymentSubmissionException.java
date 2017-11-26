package edu.sc.csce740.exception;

/**
 * Encounters if errors happen when submitting payment changes.
 */
public class PaymentSubmissionException extends Exception {
    public PaymentSubmissionException(String s) {
        super(s);
    }
}
