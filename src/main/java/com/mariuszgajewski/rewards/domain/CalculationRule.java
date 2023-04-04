package com.mariuszgajewski.rewards.domain;

import com.mariuszgajewski.rewards.domain.model.Reward;
import com.mariuszgajewski.rewards.domain.model.Transaction;

import java.util.List;

public interface CalculationRule {
    List<Reward> calculateMonthReward(List<Transaction> transactions);
}
