package com.mariuszgajewski.rewards.adapters.api.validator;

import com.mariuszgajewski.rewards.adapters.api.TransactionRequest;

public interface RequestValidator {
    void validateRequest(TransactionRequest request);
}
