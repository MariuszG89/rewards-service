package com.mariuszgajewski.rewards.domain;

import com.mariuszgajewski.rewards.domain.model.Reward;
import com.mariuszgajewski.rewards.domain.model.Transaction;
import com.mariuszgajewski.rewards.domain.model.TransactionAmount;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StandardCalculationRuleTest {

    private static final double TRANSACTION_AMOUNT_1 = 100.0;
    private static final double TRANSACTION_AMOUNT_2 = 120.0;
    private static final String TRANSACTION_CURRENCY = "USD";
    private static final String USER_ID = "USER1";
    private static final ZonedDateTime TRANSACTION_DATE = ZonedDateTime.parse("2023-04-03T12:05:45.231Z");

    private final CalculationRule calculationRule = new StandardCalculationRule();


    @Test
    void CalculateMonthRewardTest() {
        //given
        List<Transaction> list = new ArrayList<>();
        list.add(new Transaction(1, USER_ID, new TransactionAmount(TRANSACTION_AMOUNT_1, TRANSACTION_CURRENCY), TRANSACTION_DATE));
        list.add(new Transaction(2, USER_ID, new TransactionAmount(TRANSACTION_AMOUNT_2, TRANSACTION_CURRENCY), TRANSACTION_DATE));

        //then
        List<Reward> rewards = calculationRule.calculateMonthReward(list);

        //when
        assertThat(rewards).hasSize(2);
        assertThat(rewards.get(0).getValue()).isEqualTo(50);
        assertThat(rewards.get(0).getTransactionId()).isEqualTo(1);
        assertThat(rewards.get(1).getValue()).isEqualTo(90);
        assertThat(rewards.get(1).getTransactionId()).isEqualTo(2);
    }

    @Test
    void CalculateMonthRewardEmptyListTest() {
        //given
        List<Transaction> list = new ArrayList<>();

        //when
        List<Reward> rewards = calculationRule.calculateMonthReward(list);

        //then
        assertThat(rewards).isEmpty();
    }

    @Test
    void CalculateMonthRewardNullTest() {
        //when
        List<Reward> rewards = calculationRule.calculateMonthReward(null);

        //then
        assertThat(rewards).isEmpty();
    }
}