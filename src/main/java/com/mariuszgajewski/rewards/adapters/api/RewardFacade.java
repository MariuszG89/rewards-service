package com.mariuszgajewski.rewards.adapters.api;

import com.mariuszgajewski.rewards.adapters.api.validator.RequestValidator;
import com.mariuszgajewski.rewards.domain.RewardService;
import com.mariuszgajewski.rewards.domain.model.Reward;
import com.mariuszgajewski.rewards.domain.model.Rewards;
import com.mariuszgajewski.rewards.domain.model.Transaction;
import com.mariuszgajewski.rewards.domain.model.TransactionAmount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RewardFacade {
    private final RewardService rewardService;
    private final RequestValidator createTransactionRequestValidator;

    public TransactionResponse createTransaction(CreateTransactionsRequest request) {
        createTransactionRequestValidator.validateRequest(request);
        List<Transaction> transactions = new ArrayList<>();
        for (UserTransaction userTransaction : request.getTransactions()) {
            transactions.add(mapToTransaction(userTransaction, request.getUserId()));
        }
        transactions = rewardService.create(transactions);
        TransactionResponse response = new TransactionResponse();
        response.setUserId(request.getUserId());
        response.setTransactions(transactions.stream().map(this::mapToUserTransaction).toList());
        return response;
    }

    public TransactionResponse updateTransaction(UpdateTransactionRequest request, long transactionId) {
        Transaction transaction = mapToTransaction(request.getTransaction(), request.getUserId());
        transaction.setTransactionId(transactionId);
        transaction = rewardService.update(transaction);

        TransactionResponse response = new TransactionResponse();
        response.setUserId(transaction.getCustomerId());
        response.setTransactions(List.of(mapToUserTransaction(transaction)));
        return response;
    }

    public TransactionResponse removeTransaction(long transactionId) {
        Transaction transaction = Transaction.builder().transactionId(transactionId).build();
        transaction = rewardService.remove(transaction);

        TransactionResponse response = new TransactionResponse();
        response.setUserId(transaction.getCustomerId());
        response.setTransactions(List.of(mapToUserTransaction(transaction)));
        return response;
    }

    public TransactionResponse getTransaction(long transactionId) {
        Transaction transaction = rewardService.get(transactionId);

        TransactionResponse response = new TransactionResponse();
        response.setUserId(transaction.getCustomerId());
        response.setTransactions(List.of(mapToUserTransaction(transaction)));
        return response;
    }

    public CalculationResponse calculateUserReward(String customerId) {
        Rewards customerRewards = rewardService.calculateCustomerRewardPoints(customerId);
        return CalculationResponse.builder()
                .customerId(customerRewards.getCustomerId())
                .totalValue(customerRewards.getTotalValue())
                .monthlyRewards(customerRewards.getRewards().entrySet().stream().collect(
                        Collectors.toMap(Map.Entry::getKey, v -> v.getValue().stream().mapToLong(Reward::getValue).sum())))
                .build();
    }

    private Transaction mapToTransaction(UserTransaction userTransaction, String customerId) {
        return Transaction.builder()
                .transactionDate(userTransaction.getTransactionDate())
                .customerId(customerId)
                .amount(
                        TransactionAmount.builder().currency(userTransaction.getCurrency()).value(userTransaction.getAmount()).build())
                .build();
    }

    private UserTransaction mapToUserTransaction(Transaction transaction) {
        return UserTransaction.builder().transactionId(transaction.getTransactionId())
                .currency(transaction.getAmount().getCurrency())
                .amount(transaction.getAmount().getValue())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }


}
