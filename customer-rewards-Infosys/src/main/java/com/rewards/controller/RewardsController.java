package com.rewards.controller;

//import java.time.LocalDate;
import java.util.List;

//import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rewards.model.Customer;
import com.rewards.model.RewardsResponse;
//import com.rewards.repository.TransactionRepository;
import com.rewards.service.RewardsService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/rewards")
@Slf4j
@Validated
public class RewardsController {
	
private final RewardsService rewardsService;
    
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }
    
    @GetMapping("/calculate/{customerId}")
    public ResponseEntity<RewardsResponse> calculateRewards(
            @PathVariable @NotNull Long customerId,
            @RequestParam(defaultValue = "3") @Min(1) int months) {
        
        log.info("Received request to calculate rewards for customer: {}, months: {}", customerId, months);
        return ResponseEntity.ok(rewardsService.calculateRewards(customerId, months));
    }
    
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        log.info("Fetching all customers");
        return ResponseEntity.ok(rewardsService.getAllCustomers());
    }
}
