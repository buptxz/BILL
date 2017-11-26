package edu.sc.csce740.exception;

/**
 * Encounters if errors happen in generating {@link edu.sc.csce740.model.Bill}.
 */
public class BillGenerationException extends Exception {
    public BillGenerationException(String s) {
        super(s);
    }
}
