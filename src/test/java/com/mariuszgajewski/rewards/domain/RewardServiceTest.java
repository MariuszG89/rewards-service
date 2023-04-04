package com.mariuszgajewski.rewards.domain;

import com.mariuszgajewski.rewards.domain.model.Rewards;
import com.mariuszgajewski.rewards.domain.model.Transaction;
import com.mariuszgajewski.rewards.domain.model.TransactionAmount;
import com.mariuszgajewski.rewards.domain.ports.TransactionRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class RewardServiceTest {

    private static final double TRANSACTION_AMOUNT_1 = 80.0;
    private static final double TRANSACTION_AMOUNT_2 = 120.0;
    private static final String TRANSACTION_CURRENCY = "USD";
    private static final String USER_ID = "USER1";
    private static final ZonedDateTime TRANSACTION_DATE = ZonedDateTime.parse("2023-04-03T12:05:45.231Z");

    private final TransactionRepository repository = mock(TransactionRepository.class);

    private final RewardService rewardService = new RewardService(repository, new StandardCalculationRule());

    @Test
    void calculateCustomerRewardPointsTest() {
        //given
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(new Transaction(1, USER_ID, new TransactionAmount(TRANSACTION_AMOUNT_1, TRANSACTION_CURRENCY), TRANSACTION_DATE));
        transactionList.add(new Transaction(2, USER_ID, new TransactionAmount(TRANSACTION_AMOUNT_1, TRANSACTION_CURRENCY), TRANSACTION_DATE.minusDays(1)));
        transactionList.add(new Transaction(3, USER_ID, new TransactionAmount(TRANSACTION_AMOUNT_2, TRANSACTION_CURRENCY), TRANSACTION_DATE.minusMonths(1)));
        transactionList.add(new Transaction(4, USER_ID, new TransactionAmount(TRANSACTION_AMOUNT_2, TRANSACTION_CURRENCY), TRANSACTION_DATE.minusMonths(2)));
        transactionList.add(new Transaction(5, USER_ID, new TransactionAmount(TRANSACTION_AMOUNT_1, TRANSACTION_CURRENCY), TRANSACTION_DATE.minusMonths(2).minusDays(1)));

        when(repository.getCustomerTransactionNotOlderThan(anyString(), any())).thenReturn(transactionList);

        //when
        Rewards rewards = rewardService.calculateCustomerRewardPoints(USER_ID);

        //then
        assertThat(rewards.getCustomerId()).isEqualTo(USER_ID);
        assertThat(rewards.getRewards()).hasSize(3);
        assertThat(rewards.getTotalValue()).isEqualTo(270);
        assertThat(rewards.getRewards().get("April")).hasSize(2);
        assertThat(rewards.getRewards().get("April").get(0).getValue()).isEqualTo(30);
        assertThat(rewards.getRewards().get("April").get(1).getValue()).isEqualTo(30);
        assertThat(rewards.getRewards().get("March").get(0).getValue()).isEqualTo(90);
    }

    @Test
    void calculateCustomerRewardPointsNoTransactionsTest() {
        //given
        when(repository.getCustomerTransactionNotOlderThan(anyString(), any())).thenReturn(new ArrayList<>());

        //when
        Rewards rewards = rewardService.calculateCustomerRewardPoints(USER_ID);

        //then
        assertThat(rewards.getCustomerId()).isEqualTo(USER_ID);
        assertThat(rewards.getRewards()).isEmpty();
        assertThat(rewards.getTotalValue()).isZero();
    }


    @Test
    void createTransactionsTest() {
        //given
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(new Transaction(1, USER_ID, new TransactionAmount(TRANSACTION_AMOUNT_1, TRANSACTION_CURRENCY), TRANSACTION_DATE));
        transactionList.add(new Transaction(2, USER_ID, new TransactionAmount(TRANSACTION_AMOUNT_2, TRANSACTION_CURRENCY), TRANSACTION_DATE.minusMonths(1)));

        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        //when
        List<Transaction> result = rewardService.create(transactionList);

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(transactionList.get(0));
        assertThat(result.get(1)).isEqualTo(transactionList.get(1));
    }

    @Test
    void createTransactionsNullTest() {
        List<Transaction> result = rewardService.create(null);
        assertThat(result).isEmpty();
    }

    @Test
    void createTransactionsEmptyTest() {
        List<Transaction> result = rewardService.create(new ArrayList<>());
        assertThat(result).isEmpty();
    }
}