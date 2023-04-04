package com.mariuszgajewski.rewards.domain;

import com.mariuszgajewski.rewards.domain.model.Reward;
import com.mariuszgajewski.rewards.domain.model.Transaction;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StandardCalculationRule implements CalculationRule {

    private static final int FIRST_THRESHOLD_LEVEL = 50;
    private static final int THRESHOLD_LEVEL_SECOND = 100;
    private static final int FIRST_THRESHOLD_LEVEL_POINTS_MULTIPLIER = 1;
    private static final int SECOND_THRESHOLD_LEVEL_POINTS_MULTIPLIER = 2;

    @Override
    public List<Reward> calculateMonthReward(List<Transaction> transactions) {
        List<Reward> rewards = new ArrayList<>();
        if (CollectionUtils.isEmpty(transactions)) {
            return rewards;
        }

        for (Transaction transaction : transactions) {
            long rewardPoints = 0L;
            Double transactionAmount = transaction.getAmount().getValue();
            if (isAboveSecondThreshold(transactionAmount)) {
                long valueAboveThreshold = (long) (transactionAmount - THRESHOLD_LEVEL_SECOND);
                rewardPoints += SECOND_THRESHOLD_LEVEL_POINTS_MULTIPLIER * valueAboveThreshold + FIRST_THRESHOLD_LEVEL * FIRST_THRESHOLD_LEVEL_POINTS_MULTIPLIER;
            }
            else if (isAboveFirstThreshold(transactionAmount)) {
                long valueAboveThreshold = (long) (transactionAmount - FIRST_THRESHOLD_LEVEL);
                rewardPoints += FIRST_THRESHOLD_LEVEL_POINTS_MULTIPLIER * valueAboveThreshold;
            }
            rewards.add(new Reward(rewardPoints, transaction.getTransactionId()));
        }
        return rewards;
    }

    private boolean isAboveFirstThreshold(Double amount) {
        return amount > FIRST_THRESHOLD_LEVEL;
    }

    private boolean isAboveSecondThreshold(Double amount) {
        return amount > THRESHOLD_LEVEL_SECOND;
    }

}
