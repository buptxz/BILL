package edu.sc.csce740.exception;

/**
 * Exception when encounters illegal permission, such as when
 * {@link edu.sc.csce740.enums.Role} is not ADMIN.
 */
public class IllegalPermissionException extends Exception {
    public IllegalPermissionException(String s) {
        super(s);
    }
}
