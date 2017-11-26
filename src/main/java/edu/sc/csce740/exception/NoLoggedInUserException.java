package edu.sc.csce740.exception;

/**
 * Encounters when the current logged in user does not present in the user id set.
 */
public class NoLoggedInUserException extends Exception {
    public NoLoggedInUserException() {
        super("No current user");
    }
}
