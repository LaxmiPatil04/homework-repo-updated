package com.rewards.service;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rewards.exception.CustomerNotFoundException;
import com.rewards.exception.InvalidMonthsException;
import com.rewards.exception.NullInputException;
import com.rewards.model.Customer;
import com.rewards.model.RewardsResponse;
import com.rewards.model.Transaction;
import com.rewards.repository.CustomerRepository;
import com.rewards.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RewardsService {
    
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    
    public RewardsService(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }
    
    public RewardsResponse calculateRewards(Long customerId, Integer months) {
        log.debug("Calculating rewards for customer {} for last {} months", customerId, months);
        
        if (customerId == null) {
            throw new NullInputException("Customer ID cannot be null");
        }
        
        if (months == null || months < 1 || months > 12) {
            throw new InvalidMonthsException("Months must be between 1 and 12");
        }
        
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found: " + customerId);
        }
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(months);
        
        List<Transaction> customerTransactions = transactionRepository
            .findByCustomerIdAndDateBetween(customerId, startDate, endDate);
            
        if (customerTransactions.isEmpty()) {
            log.info("No transactions found for customer {} in the specified period", customerId);
            return createEmptyResponse(customerId);
        }
        
        Map<String, Integer> monthlyPoints = customerTransactions.stream()
            .collect(Collectors.groupingBy(
                t -> t.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                Collectors.summingInt(this::calculatePointsForTransaction)
            ));
            
        RewardsResponse response = new RewardsResponse();
        response.setCustomerId(customerId);
        response.setMonthlyPoints(monthlyPoints);
        response.setTotalPoints(monthlyPoints.values().stream().mapToInt(Integer::intValue).sum());
        
        log.debug("Calculated rewards for customer {}: {}", customerId, response);
        return response;
    }
    
    private RewardsResponse createEmptyResponse(Long customerId) {
        RewardsResponse response = new RewardsResponse();
        response.setCustomerId(customerId);
        response.setMonthlyPoints(new HashMap<>());
        response.setTotalPoints(0);
        return response;
    }
    
    int calculatePointsForTransaction(Transaction transaction) {
        double amount = transaction.getAmount();
        int points = 0;
        
        if (amount > 100) {
            points += (amount - 100) * 2;
            points += 50;
        } else if (amount > 50) {
            points += (amount - 50);
        }
        
        return (int) points;
    }

    public List<Customer> getAllCustomers() {
        log.debug("Fetching all customers");
        return customerRepository.findAll();
    }
}
