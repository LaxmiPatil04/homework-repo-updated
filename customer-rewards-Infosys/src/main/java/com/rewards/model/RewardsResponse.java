package com.rewards.model;

import java.util.Map;

import lombok.Data;

@Data
public class RewardsResponse {
    private Long customerId;
    private Map<String, Integer> monthlyPoints;
    private Integer totalPoints;
}

