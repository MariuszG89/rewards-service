package com.mariuszgajewski.rewards.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class Rewards {
    private String customerId;
    private long totalValue;
    private Map<String, List<Reward>> rewards;
}
