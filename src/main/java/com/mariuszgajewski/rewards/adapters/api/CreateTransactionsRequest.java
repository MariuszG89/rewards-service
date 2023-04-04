package com.mariuszgajewski.rewards.adapters.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateTransactionsRequest  extends TransactionRequest {
    @JsonProperty("transactions")
    private List<UserTransaction> transactions;
}
