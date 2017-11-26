package edu.sc.csce740.exception;

/**
 * Exception when loading user to the system.
 */
public class LoadUserException extends Exception {
    public LoadUserException(String s) {
        super(s);
    }
}
