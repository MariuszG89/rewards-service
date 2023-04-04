package com.mariuszgajewski.rewards.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Transaction {
    private long transactionId;
    private String customerId;
    private TransactionAmount amount;
    private ZonedDateTime transactionDate;
}
