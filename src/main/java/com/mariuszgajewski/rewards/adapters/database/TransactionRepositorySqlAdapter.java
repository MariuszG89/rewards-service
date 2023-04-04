package com.mariuszgajewski.rewards.adapters.database;

import com.mariuszgajewski.rewards.domain.exception.ErrorMessage;
import com.mariuszgajewski.rewards.domain.exception.TransactionNotFoundException;
import com.mariuszgajewski.rewards.domain.model.Transaction;
import com.mariuszgajewski.rewards.domain.model.TransactionAmount;
import com.mariuszgajewski.rewards.domain.ports.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
@Qualifier("transactionRepository")
public class TransactionRepositorySqlAdapter implements TransactionRepository {

    private final TransactionSqlRepository sqlRepository;

    @Override
    public List<Transaction> getCustomerTransactionNotOlderThan(String customerId, ZonedDateTime fromDate) {
        return sqlRepository.getCustomerTransactionFromDate(customerId, fromDate);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return toTransaction(sqlRepository.save(TransactionModel.of(transaction)));
    }

    @Override
    public Transaction update(Transaction transaction) {
        TransactionModel model = sqlRepository.findById(transaction.getTransactionId()).orElseThrow(() -> new TransactionNotFoundException(ErrorMessage.TRANSACTION_NOT_FOUND));
        model.setTransactionDate(transaction.getTransactionDate());
        model.setCurrency(transaction.getAmount().getCurrency());
        model.setTransactionValue(transaction.getAmount().getValue());
        return toTransaction(sqlRepository.save(model));
    }

    @Override
    public Transaction remove(Transaction transaction) {
        Transaction fetchedTransaction = get(transaction.getTransactionId());
        sqlRepository.deleteById(transaction.getTransactionId());
        return fetchedTransaction;
    }

    @Override
    public Transaction get(long transactionId) {
        return toTransaction(sqlRepository.findById(transactionId).orElseThrow(() -> new TransactionNotFoundException(ErrorMessage.TRANSACTION_NOT_FOUND)));
    }

    private Transaction toTransaction(TransactionModel transactionModel) {
        return Transaction.builder()
                .transactionId(transactionModel.getId())
                .customerId(transactionModel.getCustomerId())
                .amount(TransactionAmount.builder()
                        .currency(transactionModel.getCurrency())
                        .value(transactionModel.getTransactionValue())
                        .build())
                .transactionDate(transactionModel.getTransactionDate())
                .build();
    }
}
