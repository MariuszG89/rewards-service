package com.mariuszgajewski.rewards.adapters.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserTransaction {
    private ZonedDateTime transactionDate;
    @JsonProperty("transactionCurrency")
    @Schema(description = "ISO 4217 currency code", example = "USD")
    private String currency;
    @JsonProperty("transactionAmount")
    @Schema(description = "Transaction amount", example = "20.6")
    private Double amount;
    @JsonProperty
    private long transactionId;
}
