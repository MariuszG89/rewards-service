package com.mariuszgajewski.rewards.adapters.database;


import com.mariuszgajewski.rewards.domain.model.Transaction;
import com.mariuszgajewski.rewards.domain.model.TransactionAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "TRANSACTIONS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String customerId;
    @Column

    private String currency;

    @Column(name = "date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime transactionDate;

    @Column(name = "amount")
    private Double transactionValue;

    public static TransactionModel of(Transaction transaction) {
        return TransactionModel.builder()
                .customerId(transaction.getCustomerId())
                .transactionValue(transaction.getAmount().getValue())
                .currency(transaction.getAmount().getCurrency())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}
