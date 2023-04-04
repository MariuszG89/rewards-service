package com.mariuszgajewski.rewards.adapters.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RewardsResponse {
    @JsonProperty("customer")
    private String userId;
    @JsonProperty("rewardsDetails")
    private Map<String, Long> rewards;
    @JsonProperty("rewardPoints")
    private Long totalValue;
}
