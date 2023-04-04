package com.mariuszgajewski.rewards.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reward {
    private long value;
    private long transactionId;
}
