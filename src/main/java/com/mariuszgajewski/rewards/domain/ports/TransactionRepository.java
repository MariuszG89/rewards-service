package com.mariuszgajewski.rewards.domain.ports;

import com.mariuszgajewski.rewards.domain.model.Transaction;

import java.time.ZonedDateTime;
import java.util.List;

public interface TransactionRepository {
    List<Transaction> getCustomerTransactionNotOlderThan(String customerId, ZonedDateTime from);
    Transaction save(Transaction transaction);
    Transaction update(Transaction transaction);
    Transaction remove(Transaction transaction);
    Transaction get(long transactionId);
}
