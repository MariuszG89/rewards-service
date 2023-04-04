package com.mariuszgajewski.rewards.adapters.api.validator;

import com.mariuszgajewski.rewards.adapters.api.CreateTransactionsRequest;
import com.mariuszgajewski.rewards.adapters.api.TransactionRequest;
import com.mariuszgajewski.rewards.adapters.api.UserTransaction;
import com.mariuszgajewski.rewards.domain.exception.ErrorMessage;
import com.mariuszgajewski.rewards.domain.exception.RequestValidationErrorException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class CreateTransactionRequestValidator implements RequestValidator {
    @Override
    public void validateRequest(TransactionRequest r) {
        CreateTransactionsRequest request = (CreateTransactionsRequest) r;
        if (StringUtils.isBlank(request.getUserId())) {
            throw new RequestValidationErrorException(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR, "Client Id is missing");
        }
        if (CollectionUtils.isEmpty(request.getTransactions())) {
            throw new RequestValidationErrorException(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR, "Transactions are missing");
        }
        request.getTransactions().forEach(this::validateUserTransaction);
    }

    private void validateUserTransaction(UserTransaction userTransaction) {
        if (userTransaction.getTransactionDate() == null) {
            throw new RequestValidationErrorException(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR, "Transaction date is missing");
        }
        if (userTransaction.getAmount() == null) {
            throw new RequestValidationErrorException(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR, "Transaction amount is missing");
        }
        if (userTransaction.getAmount() < 0) {
            throw new RequestValidationErrorException(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR, "Invalid transaction amount");
        }

        if (StringUtils.isBlank(userTransaction.getCurrency())) {
            throw new RequestValidationErrorException(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR, "Transaction currency is missing");
        }
        try {
            Currency.getInstance(userTransaction.getCurrency());
        } catch (IllegalArgumentException e) {
            throw new RequestValidationErrorException(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR, "Invalid transaction currency");
        }
    }
}
