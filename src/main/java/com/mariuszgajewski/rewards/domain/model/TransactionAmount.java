package com.mariuszgajewski.rewards.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TransactionAmount {
    private Double value;
    private String currency;
}
