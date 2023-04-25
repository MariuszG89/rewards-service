package com.mariuszgajewski.rewards.adapters.api;

import com.mariuszgajewski.rewards.domain.exception.ApiErrorResponse;
import com.mariuszgajewski.rewards.domain.exception.ErrorMessage;
import com.mariuszgajewski.rewards.domain.exception.TransactionNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomExceptionHandlerTest {
    private final static long TRANSACTION_ID = 100;

    @Mock
    private ServletWebRequest webRequest = mock(ServletWebRequest.class);

    @Mock
    final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

    private final CustomExceptionHandler instance = new CustomExceptionHandler();

    @Test
    void generateLogMessageAndApiErrors()
    {
        //given
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        String path = "/rewards-service/transaction/{transactionId}";
        ErrorMessage errorMessage = ErrorMessage.TRANSACTION_NOT_FOUND;
        TransactionNotFoundException e = new TransactionNotFoundException(errorMessage,
                new ExecutionException("Message of ExecutionException", new NoSuchElementException()));

        when(httpServletRequest.getRequestURI()).thenReturn(path);
        when(webRequest.getRequest()).thenReturn(httpServletRequest);
        when(webRequest.getAttribute(matches("org.springframework.web.servlet.View.pathVariables"), anyInt()))
                .thenReturn(Map.of("transactionId", TRANSACTION_ID));

        // when
        ApiErrorResponse apiErrorsResponse = instance.transactionNotFoundException(e, webRequest).getBody();

        // then
        String logMsg = instance.prepareLogStatement(webRequest, e);
        assertThat("Error " + e.getClass().getSimpleName() + " occur due to " + errorMessage.getErrorMessage()
                + " - for: " + path + ", transactionId: " + TRANSACTION_ID).isEqualTo(logMsg);
        assertThat(apiErrorsResponse.getHttpStatusCode()).isEqualTo(httpStatus.value());
        assertThat(apiErrorsResponse.getApiErrorMessage()).isEqualTo(errorMessage.getErrorCode());
        assertThat(apiErrorsResponse.getErrorMessage()).isEqualTo(errorMessage.getErrorMessage());
        assertThat(apiErrorsResponse.getRequestPath()).isNotNull().isNotEmpty().isNotBlank().containsPattern("(/.*)+");
        assertThat(String.valueOf(TRANSACTION_ID)).isEqualTo(apiErrorsResponse.getResourceInformation().get("transactionId"));
    }

    @Test
    void shouldHandleNullPointerExceptionInTheCode()
    {
        //given
        NullPointerException npe = new NullPointerException("NPE!");

        when(httpServletRequest.getRequestURI()).thenReturn("/path/path");
        when(webRequest.getRequest()).thenReturn(httpServletRequest);

        // when
        ResponseEntity<Void> responseEntity = instance.exception(npe, webRequest);

        //then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}