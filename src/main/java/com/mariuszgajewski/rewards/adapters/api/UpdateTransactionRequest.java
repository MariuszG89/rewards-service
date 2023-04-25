package com.mariuszgajewski.rewards.adapters.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateTransactionRequest extends TransactionRequest {
    @JsonProperty("transaction")
    private UserTransaction transaction;
}
