package com.jeancy.sms.exception;

/**
 *
 * @author Jeancy Tshibemba
 */
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
