package edu.sc.csce740.exception;

/**
 * Encounters when the input date is incorrect.
 */
public class WrongDateException extends RuntimeException{
    public WrongDateException() {
        super("Wrong input date.");
    }
}
