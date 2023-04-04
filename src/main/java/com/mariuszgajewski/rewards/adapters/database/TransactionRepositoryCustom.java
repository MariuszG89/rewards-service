package com.mariuszgajewski.rewards.adapters.database;

import com.mariuszgajewski.rewards.domain.model.Transaction;

import java.time.ZonedDateTime;
import java.util.List;

public interface TransactionRepositoryCustom {
    List<Transaction> getCustomerTransactionFromDate(String customerId, ZonedDateTime fromDate);
}
