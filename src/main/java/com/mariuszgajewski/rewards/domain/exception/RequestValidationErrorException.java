package com.mariuszgajewski.rewards.domain.exception;

public class RequestValidationErrorException extends RewardException {

    protected RequestValidationErrorException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    protected RequestValidationErrorException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public RequestValidationErrorException(final ErrorMessage errorMessage, final String additionalMessage)
    {
        super(errorMessage, additionalMessage);
    }
}
