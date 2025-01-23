package com.rewards.exception;

@SuppressWarnings("serial")
public class InvalidMonthsException extends RuntimeException {
    public InvalidMonthsException(String message) {
        super(message);
    }
}
