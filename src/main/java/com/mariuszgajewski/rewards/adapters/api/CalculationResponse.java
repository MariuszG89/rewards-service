package com.mariuszgajewski.rewards.adapters.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CalculationResponse {
    private String customerId;
    private long totalValue;
    private Map<String, Long> monthlyRewards;
}
