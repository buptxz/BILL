package edu.sc.csce740.exception;

/**
 * Exception when encounters illegal permission, such as when
 * {@link edu.sc.csce740.enums.Role} is not ADMIN.
 */
public class PermissionDeniedException extends Exception {
    public PermissionDeniedException() {
        super("Permission denied. You don't have access to this operation");
    }

    public PermissionDeniedException(String s) {
        super(s);
    }
}
