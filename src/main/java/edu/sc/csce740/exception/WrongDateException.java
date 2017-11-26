package edu.sc.csce740.exception;

public class WrongDateException extends RuntimeException{
    public WrongDateException() {
        super("Wrong input date.");
    }
}
