package com.rewards.exception;

@SuppressWarnings("serial")
public class NullInputException extends RuntimeException {
    public NullInputException(String message) {
        super(message);
    }
}
