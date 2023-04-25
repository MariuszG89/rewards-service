package com.mariuszgajewski.rewards.domain;


import com.mariuszgajewski.rewards.domain.model.Reward;
import com.mariuszgajewski.rewards.domain.model.Rewards;
import com.mariuszgajewski.rewards.domain.model.Transaction;
import com.mariuszgajewski.rewards.domain.ports.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final TransactionRepository transactionRepository;
    private final CalculationRule calculationRule;

    public List<Transaction> create(List<Transaction> transactions) {
        if (CollectionUtils.isEmpty(transactions)) {
            return new ArrayList<>();
        }
        List<Transaction> savedTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            savedTransactions.add(transactionRepository.save(transaction));
        }
        return savedTransactions;
    }

    public Transaction update(Transaction transaction) {
        return transactionRepository.update(transaction);
    }

    public Transaction remove(Transaction transaction) {
        return transactionRepository.remove(transaction);
    }

    public Transaction get(long transactionId) {
        return transactionRepository.get(transactionId);
    }

    public Rewards calculateCustomerRewardPoints(String customerId) {
        List<Transaction> transactions = transactionRepository.getCustomerTransactionNotOlderThan(customerId, ZonedDateTime.now(ZoneOffset.UTC).minusMonths(3));
        Map<String, List<Transaction>> transactionsByMonths = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getTransactionDate().getMonth().getDisplayName(TextStyle.FULL, Locale.US), Collectors.toList()));
        Map <String, List<Reward>> monthlyRewards =  transactionsByMonths.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, v -> calculationRule.calculateMonthReward(v.getValue())));
        return Rewards.builder()
                .customerId(customerId)
                .rewards(monthlyRewards)
                .totalValue(monthlyRewards.values().stream().flatMap(Collection::stream).mapToLong(Reward::getValue).sum())
                .build();
    }

}
