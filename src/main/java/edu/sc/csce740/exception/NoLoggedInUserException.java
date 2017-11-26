package edu.sc.csce740.exception;

public class NoLoggedInUserException extends Exception {
    public NoLoggedInUserException() {
        super("No current user");
    }
}
