package edu.sc.csce740.exception;

/**
 * Encounters when a invalid user tries to login/logout.
 */
public class InvalidUserException extends Exception {
    public InvalidUserException(String s) {
        super(s);
    }
}
