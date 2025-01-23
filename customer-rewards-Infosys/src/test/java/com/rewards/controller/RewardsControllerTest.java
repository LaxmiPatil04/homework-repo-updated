package com.rewards.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.awt.PageAttributes.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.rewards.model.Customer;
import com.rewards.model.RewardsResponse;
import com.rewards.service.RewardsService;

@WebMvcTest(RewardsController.class)
class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private RewardsService rewardsService;

    private RewardsResponse mockRewardsResponse;

   
        
    @Nested
    @DisplayName("GET /api/rewards/calculate/{customerId}")
    class CalculateRewardsTests {

        @Test
        @DisplayName("Should calculate rewards with valid customer ID and default months")
        void calculateRewards_ValidInput_DefaultMonths_ReturnsRewards() throws Exception {
            when(rewardsService.calculateRewards(eq(1L), eq(3))).thenReturn(mockRewardsResponse);

            mockMvc.perform(get("/api/rewards/calculate/1")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.customerId", is(1)))
                    .andExpect(jsonPath("$.totalPoints", is(360)))
                    .andExpect(jsonPath("$.monthlyPoints.['2024-01']", is(120)))
                    .andExpect(jsonPath("$.monthlyPoints.['2024-02']", is(90)))
                    .andExpect(jsonPath("$.monthlyPoints.['2024-03']", is(150)));

            verify(rewardsService, times(1)).calculateRewards(1L, 3);
        }

        @Test
        @DisplayName("Should calculate rewards with valid customer ID and custom months")
        void calculateRewards_ValidInput_CustomMonths_ReturnsRewards() throws Exception {
            when(rewardsService.calculateRewards(eq(1L), eq(6))).thenReturn(mockRewardsResponse);

            mockMvc.perform(get("/api/rewards/calculate/1")
                    .param("months", "6")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.customerId", is(1)));

            verify(rewardsService, times(1)).calculateRewards(1L, 6);
        }

        @Test
        @DisplayName("Should return 404 when customer not found")
        void calculateRewards_CustomerNotFound_Returns404() throws Exception {
            when(rewardsService.calculateRewards(any(), any()))
                    .thenThrow(new CustomerNotFoundException("Customer not found"));

            mockMvc.perform(get("/api/rewards/calculate/999")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 400 when months parameter is invalid")
        void calculateRewards_InvalidMonths_Returns400() throws Exception {
            when(rewardsService.calculateRewards(any(), any()))
                    .thenThrow(new InvalidMonthsException("Invalid months"));

            mockMvc.perform(get("/api/rewards/calculate/1")
                    .param("months", "13")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 when months parameter is negative")
        void calculateRewards_NegativeMonths_Returns400() throws Exception {
            mockMvc.perform(get("/api/rewards/calculate/1")
                    .param("months", "-1")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
}
