package com.mariuszgajewski.rewards.adapters.api.validator;

import com.mariuszgajewski.rewards.adapters.api.CreateTransactionsRequest;
import com.mariuszgajewski.rewards.adapters.api.UserTransaction;
import com.mariuszgajewski.rewards.domain.exception.ErrorMessage;
import com.mariuszgajewski.rewards.domain.exception.RequestValidationErrorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CreateTransactionRequestValidatorTest {

    private static final String USER_ID = "USER1";
    private static final String TRANSACTION_CURRENCY = "USD";
    private static final double TRANSACTION_AMOUNT_1 = 120;
    private static final ZonedDateTime TRANSACTION_DATE = ZonedDateTime.parse("2023-04-03T12:05:45.231Z");

    private final RequestValidator instance = new CreateTransactionRequestValidator();
    private CreateTransactionsRequest request;

    @BeforeEach
    void setUp() {
        request = new CreateTransactionsRequest();
        UserTransaction userTransaction = UserTransaction.builder()
                .transactionDate(TRANSACTION_DATE)
                .amount(TRANSACTION_AMOUNT_1)
                .currency(TRANSACTION_CURRENCY)
                .build();

        request.setUserId(USER_ID);
        request.setTransactions(List.of(userTransaction));
    }

    @AfterEach
    void tearDown() {
    }

    private void assertErrorCode(RequestValidationErrorException exception) {
        assertThat(exception.getErrorMessage()).isEqualTo(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR);
    }
    @Test
    void validateRequestMissingUserId() {
        //given
        request.setUserId(null);

        //when //then
        RequestValidationErrorException exception = assertThrows(RequestValidationErrorException.class, () -> instance.validateRequest(request));
        assertErrorCode(exception);
        assertThat(exception.getAdditionalErrorMessage()).isEqualTo("Client Id is missing");
    }

    @Test
    void validateRequestMissingTransactions() {
        //given
        request.setTransactions(null);

        //when //then
        RequestValidationErrorException exception = assertThrows(RequestValidationErrorException.class, () -> instance.validateRequest(request));
        assertErrorCode(exception);
        assertThat(exception.getAdditionalErrorMessage()).isEqualTo("Transactions are missing");
    }

    @Test
    void validateRequestMissingTransactionsDate() {
        //given
        request.getTransactions().get(0).setTransactionDate(null);

        //when //then
        RequestValidationErrorException exception = assertThrows(RequestValidationErrorException.class, () -> instance.validateRequest(request));
        assertErrorCode(exception);
        assertThat(exception.getAdditionalErrorMessage()).isEqualTo("Transaction date is missing");
    }

    @Test
    void validateRequestMissingTransactionsAmount() {
        //given
        request.getTransactions().get(0).setAmount(null);

        //when //then
        RequestValidationErrorException exception = assertThrows(RequestValidationErrorException.class, () -> instance.validateRequest(request));
        assertErrorCode(exception);
        assertThat(exception.getAdditionalErrorMessage()).isEqualTo("Transaction amount is missing");
    }

    @Test
    void validateRequestInvalidTransactionsAmount() {
        //given
        request.getTransactions().get(0).setAmount(-5.2);

        //when //then
        RequestValidationErrorException exception = assertThrows(RequestValidationErrorException.class, () -> instance.validateRequest(request));
        assertErrorCode(exception);
        assertThat(exception.getAdditionalErrorMessage()).isEqualTo("Invalid transaction amount");
    }

    @Test
    void validateRequestInvalidTransactionsCurrency() {
        //given
        request.getTransactions().get(0).setCurrency("ABC");

        //when //then
        RequestValidationErrorException exception = assertThrows(RequestValidationErrorException.class, () -> instance.validateRequest(request));
        assertErrorCode(exception);
        assertThat(exception.getAdditionalErrorMessage()).isEqualTo("Invalid transaction currency");
    }
}