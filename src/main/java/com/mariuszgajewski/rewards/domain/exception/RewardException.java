package com.mariuszgajewski.rewards.domain.exception;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class RewardException extends RuntimeException {

    private final ErrorMessage errorMessage;
    private final String additionalErrorMessage;

    protected RewardException(ErrorMessage errorMessage)
    {
        super(errorMessage.getErrorMessage());
        this.errorMessage = errorMessage;
        additionalErrorMessage = null;
    }

    protected RewardException(ErrorMessage errorMessage, Throwable cause)
    {
        super(errorMessage.getErrorMessage(), cause);
        this.errorMessage = errorMessage;
        additionalErrorMessage = null;
    }

    protected RewardException(ErrorMessage errorMessage, String additionalErrorMessage)
    {
        super(errorMessage.getErrorMessage());
        this.errorMessage = errorMessage;
        this.additionalErrorMessage = additionalErrorMessage;
    }

    @Override
    public String getMessage()
    {
        StringBuilder message = new StringBuilder();
        if (StringUtils.isNotEmpty(additionalErrorMessage))
        {
            message.append(additionalErrorMessage).append(System.lineSeparator());
        }
        message.append(super.getMessage());
        return message.toString();
    }
}
